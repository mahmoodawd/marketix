package com.example.shopify.favorites.data.remote

import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getFavoritesProducts(): DraftOrderResponse

    suspend fun<T> removeDraftOrder(id: String): Flow<Response<T>>
    suspend fun <T> getUserEmail(): Flow<Response<T>>
}