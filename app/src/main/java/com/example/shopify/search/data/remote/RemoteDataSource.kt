package com.example.shopify.search.data.remote

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun <T> searchForProducts(tag: String): Flow<Response<T>>
}