package com.example.shopify.orders.data.repository

import com.example.shopify.orders.domain.model.OrdersModel
import com.example.shopify.orders.domain.repository.OrdersRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeOrdersRepository(private val ordersModel: OrdersModel) : OrdersRepository{
    override suspend fun getCustomerOrders(customerEmail: String): Flow<Response<OrdersModel>> {
        return flowOf(
            try {
                Response.Success(OrdersModel(ordersModel.orders.filter { it.email == customerEmail }))
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }
}