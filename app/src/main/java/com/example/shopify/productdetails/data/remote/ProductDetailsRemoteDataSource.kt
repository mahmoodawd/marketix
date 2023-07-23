package com.example.shopify.productdetails.data.remote

import com.example.shopify.data.remote.ShopifyRemoteInterface
import javax.inject.Inject

class ProductDetailsRemoteDataSource @Inject constructor(
    private val remoteService: ShopifyRemoteInterface
) :
    RemoteDataSource {
    override suspend fun <T> getProductById(id: String): T =
        remoteService.getProductDetailsById(id).product as T

}