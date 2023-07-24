package com.example.shopify.checkout.domain.usecase.cart

import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteCartItemUseCase @Inject constructor(private val repository: CartAndCheckoutRepository) {

    suspend fun <T> execute(id : String) : Flow<Response<T>>
    {
        return repository.deleteItemFromCart(id)
    }

}