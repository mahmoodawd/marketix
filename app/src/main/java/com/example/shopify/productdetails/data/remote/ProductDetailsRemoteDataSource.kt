package com.example.shopify.productdetails.data.remote

import com.example.shopify.data.remote.RemoteInterface
import javax.inject.Inject

class ProductDetailsRemoteDataSource @Inject constructor(private val remoteService: RemoteInterface) :
    RemoteDataSource {
    override suspend fun <T> getProductById(id: String): T =
        remoteService.getProductDetailsById(id).product as T

}