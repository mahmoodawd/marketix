package com.example.shopify.checkout.data.local

import com.example.shopify.checkout.data.dto.response.DiscountCode
import com.example.shopify.settings.data.dto.address.AddressDto
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class FakeLocalDataSource(private val addresses : MutableList<AddressDto>,private val discountCodes : MutableList<DiscountCode>) : CartAndCheckOutLocalDataSource {
    override suspend fun <T> getAllAddress(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(addresses as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getAllDiscountCodes(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(discountCodes as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }
}