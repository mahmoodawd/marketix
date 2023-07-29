package com.example.shopify.checkout.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.auth.domain.usecases.CheckGuestStatusUseCase
import com.example.shopify.checkout.domain.model.CartItems
import com.example.shopify.checkout.domain.usecase.cart.DeleteCartItemUseCase
import com.example.shopify.checkout.domain.usecase.cart.GetCartItemsUseCase
import com.example.shopify.checkout.domain.usecase.cart.UpdateCartItemUseCase
import com.example.shopify.domain.usecase.dataStore.ReadStringFromDataStoreUseCase
import com.example.shopify.utils.hiltanotations.Dispatcher
import com.example.shopify.utils.hiltanotations.Dispatchers
import com.example.shopify.utils.response.Response
import com.example.shopify.utils.rounder.roundTo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase,
    private val updateCartItemUseCase: UpdateCartItemUseCase,
    private val readStringFromDataStoreUseCase: ReadStringFromDataStoreUseCase,
    private val checkGuestStatusUseCase: CheckGuestStatusUseCase
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

            is CartIntent.UpdateCartItem -> updateCartItem(
                intent.id,
                intent.quantity,
                intent.itemPosition
            )

            CartIntent.CheckUserIsGuest -> checkIfUserIsGuest()
        }
    }


    private fun getAllCartItems() {
        viewModelScope.launch(ioDispatcher) {
            _state.update { it.copy(loading = true) }
            getCartItemsUseCase.execute<CartItems>().collectLatest { response ->
                when (response) {
                    is Response.Failure -> _snackBarFlow.emit(R.string.failed_message)
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> {
                        _state.update {
                            it.copy(
                                loading = false,
                                cartItems = response.data?.cartItems ?: emptyList(),
                            )

                        }
                        readCurrencyFactorFromDataStore()
                    }

                }
            }
        }
    }

    private fun deleteCartItem(id: String, itemPosition: Int) {
        viewModelScope.launch(ioDispatcher)
        {
            _state.update { it.copy(loading = true) }
            deleteCartItemUseCase.execute<String>(id).collectLatest { response ->
                when (response) {
                    is Response.Failure -> _snackBarFlow.emit(R.string.failed_message)
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> {
                        (_state.value.cartItems as MutableList).removeAt(itemPosition)
                        _state.update {
                            it.copy(
                                loading = false,
                                cartItems = _state.value.cartItems.toMutableList().filter { it.itemId.toString() != id },
                                cartTotalCost = _state.value.cartItems.sumOf { it.subtotalPrice.toDouble() }.roundTo(2)
                            )
                        }
                        _snackBarFlow.emit(R.string.item_deleted_successfully)
                    }
                }
            }
        }
    }

    private fun readCurrencyFactorFromDataStore() {
        viewModelScope.launch(ioDispatcher) {
            readStringFromDataStoreUseCase.execute<String>("currencyFactor")
                .combine(readStringFromDataStoreUseCase.execute<String>("currency")) { currencyFactor, currency ->
                    when (currencyFactor) {
                        is Response.Failure -> {}
                        is Response.Loading -> {}
                        is Response.Success -> {
                            _state.update {
                                it.copy(
                                    currencyFactor = currencyFactor.data?.toDouble() ?: 1.0,
                                    currency = currency.data ?: "EGP"
                                )
                            }
                            _state.value.cartItems.forEach { cartItem ->
                                cartItem.subtotalPrice = ((cartItem.subtotalPrice.toDouble())
                                        * _state.value.currencyFactor).roundTo(2).toString()

                                cartItem.currency = currency.data ?: "EGP"
                            }
                            _state.update {
                                it.copy(cartTotalCost = _state.value.cartItems.sumOf { it.subtotalPrice.toDouble() }
                                    .roundTo(2))
                            }
                        }
                    }
                }.collect()
        }
    }

    private fun checkIfUserIsGuest() {
        viewModelScope.launch(ioDispatcher) {
            _state.update { it.copy(userIsGuest = checkGuestStatusUseCase.invoke()) }
        }
    }

    private fun updateCartItem(id: String, quantity: String, itemPosition: Int) {
        viewModelScope.launch(ioDispatcher)
        {
            if (quantity.toInt() > _state.value.cartItems[itemPosition].upperLimit) {
                _snackBarFlow.emit(R.string.the_max_mount_item)
                return@launch
            }
           // _state.update { it.copy(loading = true) }
            updateCartItemUseCase.execute<String>(id, quantity).collectLatest { response ->
                when (response) {
                    is Response.Failure -> _snackBarFlow.emit(R.string.failed_message)
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> {

                        val cartItems = _state.value.cartItems.toMutableList()
                        cartItems[itemPosition] =
                            cartItems[itemPosition].copy(
                                quantity = quantity,
                                subtotalPrice = (cartItems[itemPosition].oneItemPrice.toDouble()
                                        * quantity.toDouble()
                                        * _state.value.currencyFactor
                                        ).roundTo(2).toString()
                            )
                        _state.update {
                            it.copy(
                                loading = false,
                                cartItems = cartItems,
                                cartTotalCost = cartItems.sumOf { it.subtotalPrice.toDouble() }.roundTo(2),
                            )
                        }
                    }
                }
            }

        }

    }


    init {
        onEvent(CartIntent.CheckUserIsGuest)


    }

}