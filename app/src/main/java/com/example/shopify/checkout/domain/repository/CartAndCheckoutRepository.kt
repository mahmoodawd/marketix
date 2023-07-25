package com.example.shopify.checkout.domain.repository

import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface CartAndCheckoutRepository {

    suspend fun <T> getCartItems() : Flow<Response<T>>

    suspend fun <T> deleteItemFromCart(id : String) : Flow<Response<T>>

    suspend fun <T> updateItemFromCart(id : String,quantity : String) : Flow<Response<T>>

    suspend fun <T> deleteDiscountCodeFromDatabase(code : DiscountCode) : Flow<Response<T>>


    suspend fun <T> getDiscountCodeById(id : String) : Flow<Response<T>>

    suspend fun <T> getAllAddress() : Flow<Response<T>>

    suspend fun <T> getAllDiscountCodes() : Flow<Response<T>>


    suspend fun <T> getUserEmail() : Flow<Response<T>>

    suspend fun<T> getUserPhone() : Flow<Response<T>>

    suspend fun <T> getPriceRule(id :String): Flow<Response<T>>
}