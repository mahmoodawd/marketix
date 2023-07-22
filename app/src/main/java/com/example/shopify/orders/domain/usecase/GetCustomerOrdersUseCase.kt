package com.example.shopify.orders.domain.usecase

import com.example.shopify.orders.domain.model.OrdersModel
import com.example.shopify.orders.domain.repository.OrdersRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCustomerOrdersUseCase @Inject constructor(private val ordersRepository: OrdersRepository) {
    suspend fun execute(customerEmail: String): Flow<Response<OrdersModel>> {
        return ordersRepository.getCustomerOrders(customerEmail)
    }
}