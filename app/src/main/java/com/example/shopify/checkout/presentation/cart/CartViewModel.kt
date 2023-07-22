package com.example.shopify.checkout.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.checkout.domain.model.CartItem
import com.example.shopify.checkout.domain.model.CartItems
import com.example.shopify.checkout.domain.usecase.DeleteCartItemUseCase
import com.example.shopify.checkout.domain.usecase.GetCartItemsUseCase
import com.example.shopify.checkout.domain.usecase.UpdateCartItemUseCase
import com.example.shopify.utils.hiltanotations.Dispatcher
import com.example.shopify.utils.hiltanotations.Dispatchers
import com.example.shopify.utils.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase
) : ViewModel() {


    private val _state: MutableStateFlow<CartState> =
        MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    private val _snackBarFlow: MutableSharedFlow<Int> =
        MutableSharedFlow()
    val snackBarFlow = _snackBarFlow.asSharedFlow()


    fun onEvent(intent: CartIntent) {
        when (intent) {
            CartIntent.GetAllCartItems -> getAllCartItems()
            is CartIntent.DeleteCartItem -> {
                deleteCartItem(intent.id, intent.itemPosition)
            }

            is CartIntent.UpdateCartItem -> updateCartItem(intent.id,intent.quantity,intent.itemPosition)
        }
    }


    private fun getAllCartItems() {
        viewModelScope.launch(ioDispatcher) {
            getCartItemsUseCase.execute<CartItems>().collectLatest { response ->
                when (response) {
                    is Response.Failure -> _snackBarFlow.emit(R.string.failed_message)
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> _state.update {
                        it.copy(
                            loading = false,
                            cartItems = response.data?.cartItems ?: emptyList()
                        )
                    }
                }
            }
        }
    }

    private fun deleteCartItem(id: String, itemPosition: Int) {
        viewModelScope.launch(ioDispatcher)
        {
            deleteCartItemUseCase.execute<String>(id).collectLatest { response ->
                when (response) {
                    is Response.Failure -> _snackBarFlow.emit(R.string.failed_message)
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> {

                        _state.update {
                            it.copy(
                                loading = false,
                                cartItems = _state.value.cartItems.drop(itemPosition + 1)
                            )
                        }
                        _snackBarFlow.emit(R.string.item_deleted_successfully)
                    }
                }
            }
        }
    }

    private fun updateCartItem(id: String, quantity: String,itemPosition: Int) {
        viewModelScope.launch(ioDispatcher)
        {
            _state.update { it.copy(loading = true) }
            updateCartItemUseCase.execute<String>(id,quantity).collectLatest { response ->
                when (response) {
                    is Response.Failure -> _snackBarFlow.emit(R.string.failed_message)
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> {

                        val cartItems = _state.value.cartItems
                        (cartItems as MutableList)[itemPosition] = cartItems[itemPosition].copy(quantity = quantity)
                        _state.update {
                            it.copy(
                                loading = false,
                             cartItems = cartItems
                            )
                        }
                        _snackBarFlow.emit(R.string.item_deleted_successfully)
                    }
                }
            }

        }

    }


}