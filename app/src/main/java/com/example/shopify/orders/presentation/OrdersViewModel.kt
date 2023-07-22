package com.example.shopify.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.orders.domain.usecase.GetCustomerOrdersUseCase
import com.example.shopify.utils.hiltanotations.Dispatcher
import com.example.shopify.utils.hiltanotations.Dispatchers
import com.example.shopify.utils.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase
) : ViewModel() {

    private val _ordersState: MutableStateFlow<OrdersState> = MutableStateFlow(OrdersState())
    val ordersState = _ordersState.asStateFlow()

    fun getCustomerOrders(customerEmail: String) {
        viewModelScope.launch(ioDispatcher) {
            _ordersState.update { it.copy(loading = true) }
            getCustomerOrdersUseCase.execute(customerEmail).collectLatest { response ->
                when (response) {
                    is Response.Success -> {
                        response.data?.let {
                            _ordersState.update {
                                it.copy(orders = response.data.orders, loading = false)
                            }
                        }
                    }

                    is Response.Failure -> {
                        _ordersState.update {
                            it.copy(error = response.error, loading = false)
                        }
                    }

                    is Response.Loading -> {
                        _ordersState.update {
                            it.copy(loading = true)
                        }
                    }
                }
            }
        }
    }
}