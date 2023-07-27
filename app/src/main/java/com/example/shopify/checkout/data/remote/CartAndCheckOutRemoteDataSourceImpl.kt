package com.example.shopify.checkout.data.remote

import android.util.Log
import com.example.shopify.checkout.data.dto.post.PostOrder
import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.example.shopify.home.data.local.DiscountCodesDao
import com.example.shopify.settings.data.local.AddressDao
import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartAndCheckOutRemoteDataSourceImpl @Inject constructor(
    private val remoteInterface: ShopifyRemoteInterface,
    private val discountCodeDao: DiscountCodesDao,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
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

    override suspend fun <T> getProductById(id: String): Flow<Response<T>> {
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

    override suspend fun <T> updateItemFromCart(id: String, quantity: String): Flow<Response<T>> {


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
                val remote = remoteInterface.getDiscountCodeById(id)
                Log.d("remoteCode", remote.toString())
                Response.Success(remote as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend  fun <T> getUserEmail(): Flow<Response<T>> {
        return flowOf(
            try {

                Response.Success(firebaseAuth.currentUser?.email as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

      override suspend fun <T> getUserPhone(): Flow<Response<T>> {
        return flowOf(
            try {
                val user = firebaseAuth.currentUser
                val phoneTask =
                    firebaseFirestore.collection("users").document(user!!.uid).get().await()


                Response.Success(phoneTask.data!![user.uid].toString() as T)

            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getPriceRule(id :String): Flow<Response<T>> {
        return flowOf(
            try {
                val remote = remoteInterface.getPriceRule(id)
                Log.d("remoteRule",remote.toString())
                Response.Success(remote as T)

            } catch (e: Exception) {
                Log.d("remoteRule",e.message!!)
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }

    override suspend fun <T> getAllCustomerAddress(
        customerId: String,

        ): Flow<Response<T>> {
        return flowOf(try {
            Response.Success(remoteInterface.getAddressesForCustomer(customerId) as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownError")
        })
    }


    override suspend fun <T> createOrder(postOrder: PostOrder): Flow<Response<T>> {
        return flowOf(
            try {
                Response.Success(remoteInterface.createOrder(postOrder) as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "UnKnown")
            }
        )
    }
    override suspend fun <T> deleteDraftOrder(id: String): Flow<Response<T>> {
        return flowOf(try {
            Response.Success(remoteInterface.deleteDraftOrder(id) as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownError")
        })
    }

    override suspend fun <T> getCustomerId(): Flow<Response<T>> {
        return flowOf(try {
            Response.Success(remoteInterface.getUserWithEmail(firebaseAuth.currentUser!!.email!!).customers.first().id.toString() as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownError")
        })

    }

}