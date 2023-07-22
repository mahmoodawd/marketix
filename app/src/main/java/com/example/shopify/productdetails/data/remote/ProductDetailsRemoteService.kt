package com.example.shopify.productdetails.data.remote

import com.example.shopify.productdetails.data.dto.ProductDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductDetailsRemoteService {

    @GET("products/{productId}.json")
    suspend fun getProductById(@Path("productId") productId: String): ProductDetailsResponse
}