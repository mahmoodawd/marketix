package com.example.shopify.checkout.data.repository

import com.example.shopify.checkout.data.mappers.toCartItems
import com.example.shopify.checkout.data.remote.CartAndCheckOutRemoteDataSource
import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartAndCheckoutRepositoryImpl @Inject constructor(private val remoteDataSource: CartAndCheckOutRemoteDataSource) :
    CartAndCheckoutRepository {
    override suspend fun <T> getCartItems(): Flow<Response<T>> {
        return remoteDataSource.getCartItems<T>()
            .map { response ->
                Response.Success((response.data as DraftOrderResponse).toCartItems() as T)
            }
    }

    override suspend fun <T> deleteItemFromCart(id: String): Flow<Response<T>> {
        return remoteDataSource.deleteItemFromCart(id)
    }

    override suspend fun <T> updateItemFromCart(id: String, quantity: String): Flow<Response<T>> {
        return remoteDataSource.updateItemFromCart(id,quantity)
    }
}