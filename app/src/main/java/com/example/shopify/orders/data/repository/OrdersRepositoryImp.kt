package com.example.shopify.orders.data.repository

import com.example.shopify.orders.data.dto.OrdersResponse
import com.example.shopify.orders.data.dto.post.PostOrder
import com.example.shopify.orders.data.dto.post.PostOrderResponse
import com.example.shopify.orders.data.mappers.toOrdersModel
import com.example.shopify.orders.data.remote.OrdersRemoteSource
import com.example.shopify.orders.domain.model.OrdersModel
import com.example.shopify.orders.domain.repository.OrdersRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class OrdersRepositoryImp @Inject constructor(
    private val ordersRemoteSource: OrdersRemoteSource
    ): OrdersRepository {
    override suspend fun getCustomerOrders(customerEmail: String): Flow<Response<OrdersModel>> {
        return try {
            ordersRemoteSource.getCustomerOrders<OrdersResponse>(customerEmail).map {
                Timber.e(it.data!!.toOrdersModel().toString())
                Response.Success(it.data!!.toOrdersModel())
            }
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "UnKnown"))
        }
    }

//    override suspend fun createOrder(postOrder: PostOrder): Flow<Response<PostOrderResponse>> {
//        return try {
//            ordersRemoteSource.createOrder(postOrder)
//        } catch (e: Exception) {
//            flowOf(Response.Failure(e.message ?: "UnKnown"))
//        }
//    }
}