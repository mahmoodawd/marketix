package com.example.shopify.checkout.data.local

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface CartAndCheckOutLocalDataSource {

    suspend fun <T> getAllAddress() : Flow<Response<T>>

    suspend fun <T> getAllDiscountCodes() : Flow<Response<T>>
}