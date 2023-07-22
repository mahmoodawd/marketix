package com.example.shopify.checkout.domain.repository

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface CartAndCheckoutRepository {

    suspend fun <T> getCartItems() : Flow<Response<T>>

    suspend fun <T> deleteItemFromCart(id : String) : Flow<Response<T>>

    suspend fun <T> updateItemFromCart(id : String,quantity : String) : Flow<Response<T>>


}