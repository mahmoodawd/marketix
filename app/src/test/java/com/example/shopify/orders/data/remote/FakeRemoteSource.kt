package com.example.shopify.orders.data.remote

import com.example.shopify.orders.data.dto.OrdersResponse
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRemoteSource(private val ordersResponse: OrdersResponse) : OrdersRemoteSource {

    override suspend fun <T> getCustomerOrders(customerEmail: String): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(OrdersResponse(ordersResponse.orders.filter { it.email == customerEmail }) as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }

}