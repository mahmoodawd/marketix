package com.example.shopify.productdetails.data.repository

import com.example.shopify.productdetails.data.dto.Product
import com.example.shopify.productdetails.data.dto.ProductDetailsResponse
import com.example.shopify.productdetails.data.mappers.toProductsDetailsModel
import com.example.shopify.productdetails.data.remote.RemoteDataSource
import com.example.shopify.productdetails.domain.repository.ProductDetailsRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProductDetailsRepoImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    ProductDetailsRepository {
    override suspend fun <T> getProductById(id: String): Flow<Response<T>> =
        flowOf(
            try {
                val product =
                    remoteDataSource.getProductById<Product>(id).toProductsDetailsModel()
                Response.Success(product as T)

            } catch (e: Exception) {
                Response.Failure(e.message ?: "unKnownError")
            }
        )
}