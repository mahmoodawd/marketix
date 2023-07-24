package com.example.shopify.orders.data.remote

import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.example.shopify.orders.data.dto.post.PostOrder
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class OrdersApiClient @Inject constructor(private val remoteInterface: ShopifyRemoteInterface) :
    OrdersRemoteSource {
    override suspend fun <T> getCustomerOrders(customerEmail: String): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(remoteInterface.getCustomerOrders(customerEmail) as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }

    override suspend fun <T> createOrder(postOrder: PostOrder): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(remoteInterface.createOrder(postOrder) as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }
}