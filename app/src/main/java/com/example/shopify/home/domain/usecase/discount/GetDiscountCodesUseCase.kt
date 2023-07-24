package com.example.shopify.home.domain.usecase.discount

import com.example.shopify.home.domain.model.ProductModel
import com.example.shopify.home.domain.repository.HomeRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiscountCodesUseCase @Inject constructor(private val repository : HomeRepository) {
    suspend fun <T> execute(): Flow<Response<T>>
    {
        return repository.getDiscountCodes<T>()
    }
}