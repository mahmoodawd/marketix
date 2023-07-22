package com.example.shopify.checkout.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.checkout.domain.model.CartItem
import com.example.shopify.checkout.domain.model.CartItems
import com.example.shopify.checkout.domain.usecase.GetCartItemsUseCase
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
    private val getCartItemsUseCase: GetCartItemsUseCase
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
        }
    }


    private fun getAllCartItems() {
        viewModelScope.launch(ioDispatcher) {
            getCartItemsUseCase.execute<CartItems>().collectLatest { response ->
                when(response)
                {
                    is Response.Failure -> _snackBarFlow.emit(R.string.failed_message)
                    is Response.Loading -> _state.update { it.copy(loading = true) }
                    is Response.Success -> _state.update { it.copy(cartItems = response.data?.cartItems ?: emptyList()) }
                }
            }
        }
    }


    init {
        onEvent(CartIntent.GetAllCartItems)
    }

}