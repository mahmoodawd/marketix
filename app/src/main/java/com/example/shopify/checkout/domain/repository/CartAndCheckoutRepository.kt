package com.example.shopify.checkout.domain.repository

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface CartAndCheckoutRepository {

    suspend fun <T> getCartItems() : Flow<Response<T>>
}