package com.example.shopify.checkout.data.remote

import com.example.shopify.checkout.data.dto.Customer
import com.example.shopify.checkout.data.dto.discountcode.DiscountCodeResponse
import com.example.shopify.checkout.data.dto.post.PostOrder
import com.example.shopify.checkout.data.dto.pricerule.PriceRule
import com.example.shopify.checkout.data.dto.pricerule.PriceRules
import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.dto.DraftOrdersItem
import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.settings.data.dto.address.AddressDto
import com.example.shopify.settings.data.dto.address.AddressResponse
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRemoteDataSource(private val carItems: DraftOrderResponse
,private val discountCodes: MutableList<com.example.shopify.checkout.data.dto.discountcode.DiscountCode>
,private val addresses: MutableList<AddressDto>
    ,private val priceRules: MutableList<PriceRule>
    , private val customers: MutableList<Customer>
    ,private val draftOrders: MutableList<DraftOrdersItem>
   , private val email: String
    , private val phone: String

) : CartAndCheckOutRemoteDataSource {

    override suspend fun <T> getCartItems(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(carItems as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getProductById(id: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> deleteItemFromCart(id: String): Flow<Response<T>> {
        return flowOf(
            try {
             (carItems.draft_orders as MutableList).removeIf{it.id == id.toLong()}
                Response.Success("successful delete" as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> updateItemFromCart(id: String, quantity: String): Flow<Response<T>> {
        return flowOf(
            try {
                carItems.draft_orders.first { it.id.toString() == id }.line_items.first().quantity = quantity.toInt()
                Response.Success("successful" as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getDiscountCodeById(id: String): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success( DiscountCodeResponse(discountCodes.firstOrNull { it.id == id.toLong() }!!) as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getUserEmail(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(email as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getUserPhone(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(phone as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getPriceRule(id: String): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success( PriceRules(priceRules.firstOrNull { it.id == id.toLong() }!!) as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getAllCustomerAddress(customerId: String): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(AddressResponse(addresses.filter { it.customer_id == customerId.toLong() }) as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> deleteDraftOrder(orderId: String): Flow<Response<T>> {
        return flowOf(
            try {
                  draftOrders.removeIf { it.id == orderId.toLong() }
                Response.Success("successfully" as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> createOrder(postOrder: PostOrder): Flow<Response<T>> {
        return flowOf(
            try {
                draftOrders.add(
                    DraftOrdersItem(email =  postOrder.order.email))
                Response.Success("successfully" as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getCustomerId(): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(customers.firstOrNull { it.email == email }?.id.toString() as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> exchangeCurrency(from: String, to: String): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(30.0 as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }
}