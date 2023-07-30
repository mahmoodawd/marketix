package com.example.shopify.favorites.data.remote

import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeFavRemoteDataSource(
    private val draftOrderResponse: DraftOrderResponse,
    private val email: String
) : RemoteDataSource {
    override suspend fun getFavoritesProducts(): DraftOrderResponse =
        draftOrderResponse

    override suspend fun removeDraftOrder(id: String) {

        (draftOrderResponse.draft_orders as MutableList).removeIf { it.id == id.toLong() }
    }

    override suspend fun <T> getUserEmail(): Flow<Response<T>> =
        flowOf(
            try {
                Response.Success(email as T)
            } catch (e: Exception) {
                Response.Failure("unKnownError")
            }
        )
}