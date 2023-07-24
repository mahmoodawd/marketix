package com.example.shopify.orders.data.remote

import com.example.shopify.orders.data.dto.post.PostOrder
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface OrdersRemoteSource {
    suspend fun <T> getCustomerOrders(customerEmail: String): Flow<Response<T>>

    suspend fun <T> createOrder(postOrder: PostOrder): Flow<Response<T>>
}