package com.example.shopify.home.data.remote.products

import com.example.shopify.data.remote.RemoteInterface
import com.example.shopify.home.data.mappers.toProductsModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProductsApiClient @Inject constructor(private val remoteInterface: RemoteInterface) :
    ProductRemoteSource {
    override suspend fun <T> getAllProducts(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(remoteInterface.getAllProducts().toProductsModel() as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }

    override suspend fun <T> getProductsByBrand(brand: String): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(remoteInterface.getProductsByBrand(brand).toProductsModel() as T)
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
                    remoteInterface.filterProducts(category, productType).toProductsModel() as T
                )
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }
}