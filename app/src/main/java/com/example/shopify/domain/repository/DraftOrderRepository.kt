package com.example.shopify.domain.repository

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface DraftOrdersRepository {
    suspend fun <T> getDraftOrders(): Flow<Response<T>>
    suspend fun removeDraftOrder(id: String): Flow<Response<*>>
}