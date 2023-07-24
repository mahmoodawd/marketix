package com.example.shopify.auth.data.remote

import com.example.shopify.auth.data.dto.CustomerResponse
import com.example.shopify.data.remote.ShopifyRemoteInterface
import javax.inject.Inject

class CustomerRemoteDataSource @Inject constructor(
    private val remoteInterface: ShopifyRemoteInterface
) : RemoteDataSource {
    override suspend fun <T> createNewCustomer(customerResponse: CustomerResponse): T =
        remoteInterface.createCustomer(customerResponse) as T

}