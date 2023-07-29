package com.example.shopify.checkout.data.repository

import com.example.shopify.checkout.data.dto.post.PostOrder
import com.example.shopify.checkout.data.dto.response.PostResponse
import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

class FakeCartAndCheckOutRepository : CartAndCheckoutRepository {

    override suspend fun <T> getCartItems(): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> deleteItemFromCart(id: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> updateItemFromCart(id: String, quantity: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> deleteDiscountCodeFromDatabase(code: DiscountCode): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getDiscountCodeById(id: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getAllCustomerAddress(customerId: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getAllDiscountCodes(): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getUserEmail(): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getUserPhone(): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getPriceRule(id: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun createOrder(postOrder: PostOrder): Flow<Response<PostResponse>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> deleteDraftOrder(id: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getCustomerId(): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> exchangeCurrency(from: String, to: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }
}