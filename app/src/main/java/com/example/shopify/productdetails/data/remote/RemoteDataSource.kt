package com.example.shopify.productdetails.data.remote

import com.example.shopify.productdetails.data.dto.draftorder.DraftOrderRequest
import com.example.shopify.utils.response.Response

interface RemoteDataSource {

    suspend fun <T> getProductById(id: String): Response<T>

    suspend fun <T> addDraftOrder(draftOrderRequest: DraftOrderRequest): Response<T>
    suspend fun <T> getDraftOrders(): Response<T>
}