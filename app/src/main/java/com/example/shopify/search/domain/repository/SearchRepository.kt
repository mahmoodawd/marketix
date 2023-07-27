package com.example.shopify.search.domain.repository

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun <T> searchProducts(tag: String): Flow<Response<T>>
}