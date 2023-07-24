package com.example.shopify.productdetails.domain.repository

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRepository {
    suspend fun <T> getProductById(id: String): Flow<Response<T>>

}