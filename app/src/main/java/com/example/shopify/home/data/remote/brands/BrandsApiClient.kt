package com.example.shopify.home.data.remote.brands

import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class BrandsApiClient @Inject constructor(private val remoteInterface: ShopifyRemoteInterface) :
    BrandsRemoteSource {
    override suspend fun <T> getAllBrands(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(remoteInterface.getAllBrands() as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }
}