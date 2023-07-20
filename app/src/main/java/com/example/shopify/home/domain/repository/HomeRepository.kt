package com.example.shopify.home.domain.repository

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun <T> getAllBrands(): Flow<Response<T>>
    suspend fun <T> getAllProducts(brand: String , id: Long): Flow<Response<T>>

    suspend fun <T> getProductsByCategory(category: Long): Flow<Response<T>>
}