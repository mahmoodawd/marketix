package com.example.shopify.checkout.data.remote

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface CartAndCheckOutRemoteDataSource {

    suspend fun <T> getCartItems() : Flow<Response<T>>

}