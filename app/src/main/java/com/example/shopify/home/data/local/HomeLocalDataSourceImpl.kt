package com.example.shopify.home.data.local

import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeLocalDataSourceImpl @Inject constructor(private val discountCodesDao: DiscountCodesDao) :
    HomeLocalDataSource {

    override suspend fun <T> insertDiscountCodeToDatabase(code: DiscountCode): Flow<Response<T>> {

        return flowOf(
            try {
                discountCodesDao.insert(code)
                Response.Success(
                    "inserted successfully" as T
                )
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )

    }

    override suspend fun <T> getAllDiscountCodeFromDatabase(): Flow<Response<T>> {
        return discountCodesDao.getAllDiscountCodes()
            .map { response -> Response.Success(response as T) }
    }
}