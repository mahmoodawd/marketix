package com.example.shopify.checkout.domain.usecase.discountcode

import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.home.data.mappers.toDiscountCode
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteDiscountCodeFromDatabaseUseCase @Inject constructor(private val repository: CartAndCheckoutRepository) {

    suspend fun <T> execute(code : DiscountCodeModel)  : Flow<Response<T>>
    {
        return  repository.deleteDiscountCodeFromDatabase(code.toDiscountCode())
    }

}