package com.example.shopify.checkout.data.local

import com.example.shopify.home.data.local.DiscountCodesDao
import com.example.shopify.settings.data.local.AddressDao
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CartAndCheckOutLocalDataSourceImpl @Inject constructor(
    private val addressDao: AddressDao,
    private val discountCodesDao: DiscountCodesDao
)  : CartAndCheckOutLocalDataSource {
    override suspend fun <T> getAllAddress(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(addressDao.getAllAddress().first() as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getAllDiscountCodes(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(discountCodesDao.getAllDiscountCodes().first() as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }


}