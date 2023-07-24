package com.example.shopify.checkout.data.repository

import android.util.Log
import android.util.LogPrinter
import com.example.shopify.checkout.data.dto.discountcode.DiscountCodeResponse
import com.example.shopify.checkout.data.mappers.toCartItems
import com.example.shopify.checkout.data.remote.CartAndCheckOutRemoteDataSource
import com.example.shopify.checkout.domain.repository.CartAndCheckoutRepository
import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.checkout.data.dto.product.Product
import com.example.shopify.checkout.data.mappers.toDiscountCodeModel
import com.example.shopify.checkout.data.local.CartAndCheckOutLocalDataSource
import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.home.data.dto.ProductsResponse
import com.example.shopify.home.data.local.HomeLocalDataSource
import com.example.shopify.home.data.mappers.toDiscountCodeModel
import com.example.shopify.settings.data.dto.location.AddressDto
import com.example.shopify.settings.data.mappers.toAddressModel
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartAndCheckoutRepositoryImpl @Inject constructor(
    private val remoteDataSource: CartAndCheckOutRemoteDataSource,
    private val localDataSource: CartAndCheckOutLocalDataSource
    ) :
    CartAndCheckoutRepository {
    override suspend fun <T> getCartItems(): Flow<Response<T>> {
        return remoteDataSource.getCartItems<T>()
            .map { response ->
                val limits = mutableListOf<Int>()

                (response.data as DraftOrderResponse).draft_orders.forEach {
                    val productResponse =
                        remoteDataSource.getProductById<Product>(it.line_items.first().product_id.toString()).first()
                Log.d("cartResponse",productResponse.data.toString())
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

    override suspend fun <T> deleteDiscountCodeFromDatabase(code: DiscountCode): Flow<Response<T>> {
        return remoteDataSource.deleteDiscountCodeFromDatabase(code)
    }

    override suspend fun <T> getDiscountCodeById(id: String): Flow<Response<T>> {
       return remoteDataSource.getDiscountCodeById<T>(id).map { Response.Success((it.data as DiscountCodeResponse).toDiscountCodeModel() as T)}
    }

    override suspend fun <T> getAllAddress(): Flow<Response<T>> {
        return  localDataSource.getAllAddress<T>().map {
            Log.d("repository","repository")
           Response.Success((it.data  as List<AddressDto>).map { it.toAddressModel() } as T)
        }
    }

    override suspend fun <T> getAllDiscountCodes(): Flow<Response<T>> {
        Log.d("repository","repository")
        return  localDataSource.getAllDiscountCodes<T>().map {
            Response.Success((it.data  as List<DiscountCode>).map { it.toDiscountCodeModel() } as T)
        }
    }

    override suspend fun <T> getUserEmail(): Flow<Response<T>> {
        return remoteDataSource.getUserEmail()
    }

    override  suspend fun <T> getUserPhone(): Flow<Response<T>> {
      return remoteDataSource.getUserPhone()
    }
}