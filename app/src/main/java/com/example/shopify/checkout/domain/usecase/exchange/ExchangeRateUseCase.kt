package com.example.shopify.checkout.domain.usecase.exchange

import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExchangeRateUseCase @Inject constructor(private val repository: CartAndCheckoutRepository) {

    suspend fun <T> execute(from  : String, to : String) : Flow<Response<T>>
    {
        return repository.exchangeCurrency(from , to)
    }
}