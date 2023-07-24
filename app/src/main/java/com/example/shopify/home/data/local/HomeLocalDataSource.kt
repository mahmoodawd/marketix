package com.example.shopify.home.data.local

import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.settings.data.dto.location.AddressDto
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface HomeLocalDataSource {
    suspend fun <T> insertDiscountCodeToDatabase(code: DiscountCode): Flow<Response<T>>


    suspend fun <T> getAllDiscountCodeFromDatabase(): Flow<Response<T>>

}