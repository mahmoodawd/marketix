package com.example.shopify.home.domain.usecase.discount

import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.home.domain.repository.HomeRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertDiscountCodesUseCase @Inject constructor(private val repository: HomeRepository) {
    suspend fun <T> execute(code: DiscountCodeModel): Flow<Response<T>> {
        return repository.insertDiscountCodeToDatabase<T>(code)
    }
}