package com.example.shopify.home.data.remote.products

import com.example.shopify.data.remote.RemoteInterface
import com.example.shopify.home.data.mappers.toProductsModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProductsApiClient @Inject constructor(private val remoteInterface: RemoteInterface): ProductRemoteSource {
    override suspend fun <T> getAllProducts(brand: String): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(remoteInterface.getAllProducts(brand).toProductsModel() as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }
}