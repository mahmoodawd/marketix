package com.example.shopify.favorites.data.remote

import com.example.shopify.data.dto.DraftOrderResponse

interface RemoteDataSource {
    suspend fun getFavoritesProducts(): DraftOrderResponse
}