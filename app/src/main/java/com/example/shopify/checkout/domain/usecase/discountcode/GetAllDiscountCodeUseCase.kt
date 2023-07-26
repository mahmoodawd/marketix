package com.example.shopify.checkout.domain.usecase.discountcode

import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllDiscountCodeUseCase @Inject constructor(private val repository: CartAndCheckoutRepository) {

    suspend fun <T> execute(): Flow<Response<T>> {
        return repository.getAllDiscountCodes()
    }
}