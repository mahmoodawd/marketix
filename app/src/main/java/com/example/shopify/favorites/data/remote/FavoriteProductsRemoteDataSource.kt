package com.example.shopify.favorites.data.remote

import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber
import javax.inject.Inject

class FavoriteProductsRemoteDataSource @Inject constructor(
    private val remoteInterface: ShopifyRemoteInterface,
    private val firebaseAuth: FirebaseAuth

) :
    RemoteDataSource {

    override suspend fun getFavoritesProducts(): DraftOrderResponse {

        val orders = remoteInterface.getDraftOrders()
        Timber.i("Fav Data Source: Done ->> ${orders.draft_orders.size}")
        return orders

    }

    override suspend fun<T> removeDraftOrder(id: String): Flow<Response<T>>  = flowOf(
        try {
            remoteInterface.removeDraftOrder(id)
            Response.Success("item deleted Successfully" as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownError")
        }
    )

    override suspend fun <T> getUserEmail(): Flow<Response<T>> {
        return flowOf(
            try {

                Response.Success(firebaseAuth.currentUser?.email as T)
            } catch (e: Exception) {
                Response.Failure(e.message ?: "unknownError")
            }
        )
    }
}