package com.example.shopify.auth.data.remote

import com.example.shopify.auth.data.mappers.toCustomer
import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class CustomerRemoteDataSource @Inject constructor(
    private val remoteInterface: ShopifyRemoteInterface
) : RemoteDataSource {
    override suspend fun <T> createNewCustomer(user: FirebaseUser): T =
        remoteInterface.createCustomer(user.toCustomer()) as T

}