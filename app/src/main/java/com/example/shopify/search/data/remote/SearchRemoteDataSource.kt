package com.example.shopify.search.data.remote

import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.example.shopify.search.data.mappers.toSearchProductsModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SearchRemoteDataSource @Inject constructor(
    private val remoteService: ShopifyRemoteInterface
) : RemoteDataSource {

    override suspend fun <T> searchForProducts(tag: String): Flow<Response<T>> =
        flowOf(
            try {
                Response.Success(remoteService.searchProducts() as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unKnownError")
            }
        )
}
