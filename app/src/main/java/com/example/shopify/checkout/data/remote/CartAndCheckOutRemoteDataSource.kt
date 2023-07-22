package com.example.shopify.checkout.data.remote

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface CartAndCheckOutRemoteDataSource {

    suspend fun <T> getCartItems() : Flow<Response<T>>

    suspend fun <T> getProductById(id : String) : Flow<Response<T>>

    suspend fun <T> deleteItemFromCart(id : String) : Flow<Response<T>>


    suspend fun <T> updateItemFromCart(id : String,quantity : String) : Flow<Response<T>>

}