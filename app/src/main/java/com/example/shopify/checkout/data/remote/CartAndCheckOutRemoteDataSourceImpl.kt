package com.example.shopify.checkout.data.remote

import android.util.Log
import com.example.shopify.data.remote.RemoteInterface
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CartAndCheckOutRemoteDataSourceImpl @Inject constructor(private val remoteInterface: RemoteInterface) :
    CartAndCheckOutRemoteDataSource {


    override suspend fun <T> getCartItems(): Flow<Response<T>> {
        val cartItems = remoteInterface.getDraftOrders()
        Log.d("cartItems", cartItems.toString())
        cartItems.draft_orders.forEach { cartItem ->
            val product =
                remoteInterface.getProductById(cartItem.line_items.first().product_id.toString())
            Log.d("cartItems", product.toString())
            (cartItem.line_items.first().properties as MutableList).add(
                (product.products?.first()?.image?.src ?: "")
            )
        }
        return flowOf(
            try {
                Response.Success(cartItems as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }


}