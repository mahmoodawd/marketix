package com.example.shopify.favorites.data.repository

import com.example.shopify.favorites.data.local.LocalDataSource
import com.example.shopify.favorites.data.mappers.toFavoritesModel
import com.example.shopify.favorites.data.remote.RemoteDataSource
import com.example.shopify.favorites.domain.repository.DraftOrdersRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : DraftOrdersRepository {

    override suspend fun <T> getDraftOrders(): Flow<Response<T>> = flowOf(
        try {
            val favoritesProducts = remoteDataSource.getFavoritesProducts()
            Timber.i("Fav Repo: Done ->> ${favoritesProducts.draft_orders.size}")
            Response.Success(favoritesProducts.toFavoritesModel() as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownException")
        }
    )

    override suspend fun removeDraftOrder(id: String) = flowOf(
        try {
            Response.Success(remoteDataSource.removeDraftOrder(id))
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownException")
        }
    )
}