package com.example.shopify.productdetails.data.remote

interface RemoteDataSource {

    suspend fun <T> getProductById(id: String): T
}