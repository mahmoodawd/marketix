package com.example.shopify.favorites.domain.repository

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun <T> getDraftOrders(): Flow<Response<T>>
    suspend fun <T> removeDraftOrder(id: String): Flow<Response<T>>
    suspend fun <T> getUserEmail(): Flow<Response<T>>
}