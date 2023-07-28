package com.example.shopify.checkout.data.repository

import android.util.Log
import com.example.shopify.checkout.data.dto.discountcode.DiscountCodeResponse
import com.example.shopify.checkout.data.dto.post.PostOrder
import com.example.shopify.checkout.data.dto.pricerule.PriceRules
import com.example.shopify.checkout.data.dto.response.PostResponse
import com.example.shopify.checkout.data.local.CartAndCheckOutLocalDataSource
import com.example.shopify.checkout.data.mappers.toCartItems
import com.example.shopify.checkout.data.mappers.toDiscountCodeModel
import com.example.shopify.checkout.data.mappers.toPriceRule
import com.example.shopify.checkout.data.remote.CartAndCheckOutRemoteDataSource
import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.home.data.mappers.toDiscountCodeModel
import com.example.shopify.settings.data.dto.address.AddressResponse
import com.example.shopify.settings.data.mappers.toAddressModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartAndCheckoutRepositoryImpl @Inject constructor(
    private val remoteDataSource: CartAndCheckOutRemoteDataSource,
    private val localDataSource: CartAndCheckOutLocalDataSource
    ) :
    CartAndCheckoutRepository {
    override suspend fun <T> getCartItems(): Flow<Response<T>> {
        return try {
            remoteDataSource.getCartItems<T>()
                .map { response ->
                    val email = getUserEmail<String>().first().data!!
                    val myCartItems =
                        (response.data as DraftOrderResponse?)?.draft_orders?.filter { it.email == email && it.tags == "cartItem" }
                    Response.Success(myCartItems?.toCartItems() as T)
                }
        }
       catch (e:Exception){
            flowOf( Response.Failure("error"))
        }
    }

    override suspend fun <T> deleteItemFromCart(id: String): Flow<Response<T>> {
        return remoteDataSource.deleteItemFromCart(id)
    }

    override suspend fun <T> updateItemFromCart(id: String, quantity: String): Flow<Response<T>> {
        return remoteDataSource.updateItemFromCart(id, quantity)
    }

    override suspend fun <T> deleteDiscountCodeFromDatabase(code : DiscountCode): Flow<Response<T>> {
        return localDataSource.deleteDiscountCode(code)
    }

    override suspend fun <T> getDiscountCodeById(id: String): Flow<Response<T>> {
       return remoteDataSource.getDiscountCodeById<T>(id).map { Response.Success((it.data as DiscountCodeResponse?)?.toDiscountCodeModel() as T)}
    }

    override suspend fun <T> getAllCustomerAddress(customerId: String): Flow<Response<T>> {
        return remoteDataSource.getAllCustomerAddress<T>(customerId).map {
            Response.Success((it.data as AddressResponse?)?.addresses?.map { it.toAddressModel() } as T)
        }
    }


    override suspend fun <T> getAllDiscountCodes(): Flow<Response<T>> {
        return  localDataSource.getAllDiscountCodes<T>().map {
            Response.Success((it.data  as List<DiscountCode>?)?.map { it.toDiscountCodeModel() } as T)
        }
    }

    override suspend fun <T> getUserEmail(): Flow<Response<T>> {
        return remoteDataSource.getUserEmail()
    }

    override  suspend fun <T> getUserPhone(): Flow<Response<T>> {
      return remoteDataSource.getUserPhone()
    }

    override suspend fun <T> getPriceRule(id: String): Flow<Response<T>> {
        return remoteDataSource.getPriceRule<T>(id).map {   Response.Success((it.data as PriceRules).toPriceRule() as T) }
    }


    override suspend fun createOrder(postOrder: PostOrder): Flow<Response<PostResponse>> {
        return try {
            remoteDataSource.createOrder(postOrder)
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "UnKnown"))
        }
    }

    override suspend fun <T> deleteDraftOrder(id: String): Flow<Response<T>> {
        return remoteDataSource.deleteDraftOrder(id)
    }


    override suspend fun <T> getCustomerId(): Flow<Response<T>> {
        return remoteDataSource.getCustomerId()

    }
}