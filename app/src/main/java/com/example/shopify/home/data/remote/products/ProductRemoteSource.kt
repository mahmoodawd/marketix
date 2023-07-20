package com.example.shopify.home.data.remote.products

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface ProductRemoteSource {
    suspend fun <T> getAllProducts(brand: String): Flow<Response<T>>
}