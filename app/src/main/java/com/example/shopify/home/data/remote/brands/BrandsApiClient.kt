package com.example.shopify.home.data.remote.brands

import com.example.shopify.data.remote.RemoteInterface
import com.example.shopify.home.data.mappers.toBrandsModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class BrandsApiClient @Inject constructor(private val remoteInterface: RemoteInterface) :
    BrandsRemoteSource {
    override suspend fun <T> getAllBrands(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(remoteInterface.getAllBrands().toBrandsModel() as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }
}