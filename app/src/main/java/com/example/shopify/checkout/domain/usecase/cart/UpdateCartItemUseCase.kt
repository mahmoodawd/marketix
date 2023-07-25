package com.example.shopify.checkout.domain.usecase.cart

import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateCartItemUseCase @Inject constructor(private val repository: CartAndCheckoutRepository) {

    suspend fun <T> execute(id : String,quantity : String) : Flow<Response<T>>
    {
        return repository.updateItemFromCart(id,quantity)
    }

}