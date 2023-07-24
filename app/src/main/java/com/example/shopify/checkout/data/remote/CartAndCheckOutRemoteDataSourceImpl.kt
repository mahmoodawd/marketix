package com.example.shopify.checkout.data.remote

import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.example.shopify.home.data.local.DiscountCodesDao
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class CartAndCheckOutRemoteDataSourceImpl @Inject constructor(
    private val remoteInterface: ShopifyRemoteInterface,
 private val discountCodeDao: DiscountCodesDao
) :
    CartAndCheckOutRemoteDataSource {


    override suspend fun <T> getCartItems(): Flow<Response<T>> {
        val cartItems = remoteInterface.getDraftOrders()
        return flowOf(
            try {
                Response.Success(cartItems as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getProductById(id : String): Flow<Response<T>> {
        return flowOf(
            try {

                val response = remoteInterface.getProductById(id).product
                Response.Success(response as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> deleteItemFromCart(id: String): Flow<Response<T>> {
        return flowOf(
            try {
                remoteInterface.removeDraftOrder(id)
                Response.Success("item deleted Successfully" as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> updateItemFromCart(id: String,quantity : String): Flow<Response<T>> {




        return flowOf(
            try {
                val order = remoteInterface.getDraftOrderById(id)
                order.draft_order.line_items.first().quantity = quantity.toInt()
                remoteInterface.updateDraftOrder(id, order)
                Response.Success("item deleted Successfully" as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> deleteDiscountCodeFromDatabase(code: DiscountCode): Flow<Response<T>> {
        return flowOf(
            try {

                discountCodeDao.delete(code.code)
                Response.Success("item deleted Successfully" as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getDiscountCodeById(id: String): Flow<Response<T>> {
        return flowOf(
            try {

                Response.Success(remoteInterface.getDiscountCodeById(id) as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }
}