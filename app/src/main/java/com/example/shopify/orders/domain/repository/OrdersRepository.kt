package com.example.shopify.orders.domain.repository

import com.example.shopify.orders.data.dto.post.PostOrder
import com.example.shopify.orders.data.dto.post.PostOrderResponse
import com.example.shopify.orders.domain.model.OrdersModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {
    suspend fun  getCustomerOrders(customerEmail: String): Flow<Response<OrdersModel>>

//    suspend fun  createOrder(postOrder: PostOrder): Flow<Response<PostOrderResponse>>
}