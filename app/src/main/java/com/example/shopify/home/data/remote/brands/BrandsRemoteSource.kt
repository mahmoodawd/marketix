package com.example.shopify.home.data.remote.brands

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface BrandsRemoteSource {
    suspend fun <T> getAllBrands(): Flow<Response<T>>
}