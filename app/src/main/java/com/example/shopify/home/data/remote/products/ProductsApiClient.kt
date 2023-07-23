package com.example.shopify.home.data.remote.products

import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProductsApiClient @Inject constructor(private val remoteInterface: ShopifyRemoteInterface) :
    ProductRemoteSource {
    override suspend fun <T> getAllProducts(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(remoteInterface.getAllProducts() as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }

    override suspend fun <T> getProductsByBrand(brand: String): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(remoteInterface.getProductsByBrand(brand) as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }

    override suspend fun <T> filterProducts(
        category: Long?,
        productType: String
    ): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(
                    remoteInterface.filterProducts(category, productType) as T
                )
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }
}