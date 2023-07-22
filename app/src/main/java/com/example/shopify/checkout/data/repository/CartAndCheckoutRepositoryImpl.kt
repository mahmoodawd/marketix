package com.example.shopify.checkout.data.repository

import com.example.shopify.checkout.data.mappers.toCartItems
import com.example.shopify.checkout.data.remote.CartAndCheckOutRemoteDataSource
import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.checkout.data.dto.product.Product
import com.example.shopify.home.data.dto.ProductsResponse
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartAndCheckoutRepositoryImpl @Inject constructor(private val remoteDataSource: CartAndCheckOutRemoteDataSource) :
    CartAndCheckoutRepository {
    override suspend fun <T> getCartItems(): Flow<Response<T>> {
        return remoteDataSource.getCartItems<T>()
            .map { response ->
                val limits = mutableListOf<Int>()
                (response.data as DraftOrderResponse).draft_orders.forEach {
                    val productResponse =
                        remoteDataSource.getProductById<Product>(it.line_items.first().product_id.toString()).first()
                    limits.add(productResponse
                        .data!!.variants.first {variant ->
                            variant.id ==
                         response.data.draft_orders.first().line_items.first().variant_id
                        }.inventory_quantity
                    )
                }
                Response.Success((response.data as DraftOrderResponse).toCartItems(limits) as T)
            }
    }

    override suspend fun <T> deleteItemFromCart(id: String): Flow<Response<T>> {
        return remoteDataSource.deleteItemFromCart(id)
    }

    override suspend fun <T> updateItemFromCart(id: String, quantity: String): Flow<Response<T>> {
        return remoteDataSource.updateItemFromCart(id, quantity)
    }
}