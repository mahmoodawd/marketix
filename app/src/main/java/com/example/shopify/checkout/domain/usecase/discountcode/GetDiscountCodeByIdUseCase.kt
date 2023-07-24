package com.example.shopify.checkout.domain.usecase.discountcode

import android.util.Log
import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.home.data.mappers.toDiscountCode
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiscountCodeByIdUseCase @Inject constructor(private val repository: CartAndCheckoutRepository) {

    suspend fun <T> execute(id : String): Flow<Response<T>> {
        Log.d("getDiscount",id)
        return repository.getDiscountCodeById(id)
    }

}