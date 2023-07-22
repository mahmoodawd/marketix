package com.example.shopify.orders.data.repository

import com.example.shopify.home.data.dto.BrandsResponse
import com.example.shopify.home.data.mappers.toBrandsModel
import com.example.shopify.orders.data.dto.OrdersResponse
import com.example.shopify.orders.data.mappers.toOrdersModel
import com.example.shopify.orders.data.remote.OrdersRemoteSource
import com.example.shopify.orders.domain.model.OrdersModel
import com.example.shopify.orders.domain.repository.OrdersRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrdersRepositoryImp @Inject constructor(
    private val ordersRemoteSource: OrdersRemoteSource
    ): OrdersRepository {
    override suspend fun getCustomerOrders(customerEmail: String): Flow<Response<OrdersModel>> {
        return try {
            ordersRemoteSource.getCustomerOrders<OrdersResponse>(customerEmail).map {
                Response.Success(it.data!!.toOrdersModel())
            }
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "UnKnown"))
        }
    }
}