package com.example.shopify.home.data.remote.products

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface ProductRemoteSource {
    suspend fun <T> getAllProducts(brand: String,id: Long): Flow<Response<T>>

    suspend fun <T> getProductsByCategory(category: Long): Flow<Response<T>>

}