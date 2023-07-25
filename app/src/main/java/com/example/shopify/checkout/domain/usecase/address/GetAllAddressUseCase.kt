package com.example.shopify.checkout.domain.usecase.address

import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAddressUseCase @Inject constructor(private val repository: CartAndCheckoutRepository) {
    suspend fun <T> execute() : Flow<Response<T>>
    {
        return repository.getAllAddress()
    }
}