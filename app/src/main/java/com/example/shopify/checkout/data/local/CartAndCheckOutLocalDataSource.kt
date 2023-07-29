package com.example.shopify.checkout.data.local

import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface CartAndCheckOutLocalDataSource {

    suspend fun <T> getAllAddress() : Flow<Response<T>>

    suspend fun <T> getAllDiscountCodes() : Flow<Response<T>>

    suspend fun <T> deleteDiscountCode(code : DiscountCode) : Flow<Response<T>>
}