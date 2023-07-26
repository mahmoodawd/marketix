package com.example.shopify.orders.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.domain.usecase.dataStore.ReadStringFromDataStoreUseCase
import com.example.shopify.orders.domain.usecase.GetCustomerOrdersUseCase
import com.example.shopify.utils.hiltanotations.Dispatcher
import com.example.shopify.utils.hiltanotations.Dispatchers
import com.example.shopify.utils.response.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase,
    private val readStringFromDataStoreUseCase: ReadStringFromDataStoreUseCase
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
//    fun createOrder(postOrder: PostOrder){
//        Timber.e(postOrder.toString())
//        viewModelScope.launch(ioDispatcher) {
//            createOrderUseCase.execute(postOrder).collectLatest {
//                when(it){
//                    is Response.Success ->{
//                        Timber.e(it.data.toString())
//                    }
//                    else->{
//                        Timber.e(it.error)
//                    }
//                }
//            }
//        }
//    }


    fun readCurrencyFactorFromDataStore() {
        viewModelScope.launch(ioDispatcher) {
            readStringFromDataStoreUseCase.execute<String>("currencyFactor")
                .combine(readStringFromDataStoreUseCase.execute<String>("currency")) { currentExchangeRate, currency ->
                    when (currentExchangeRate) {
                        is Response.Success -> {
                            _ordersState.update {
                                it.copy(
                                    exchangeRate = currentExchangeRate.data?.toDouble() ?: 1.0,
                                    currency = currency.data ?: "EGP"
                                )
                            }
                        }

                        else -> {}
                    }

                }.collect()
        }
    }
}