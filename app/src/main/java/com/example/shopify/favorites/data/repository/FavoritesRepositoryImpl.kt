package com.example.shopify.favorites.data.repository

import com.example.shopify.favorites.data.local.LocalDataSource
import com.example.shopify.favorites.data.mappers.toFavoritesModel
import com.example.shopify.favorites.data.remote.RemoteDataSource
import com.example.shopify.favorites.domain.repository.FavoritesRepository
import com.example.shopify.utils.constants.TAG_FAVORITES
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : FavoritesRepository {

    override suspend fun <T> getDraftOrders(): Flow<Response<T>> = flowOf(
        try {
            val email = getUserEmail<String>().first().data!!
            val favoritesProducts =
                remoteDataSource.getFavoritesProducts().draft_orders.filter { item ->
                    item.email == email &&
                            item.tags == TAG_FAVORITES
                }.toFavoritesModel()
            Response.Success(favoritesProducts as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownException")
        }
    )

    override suspend fun<T> removeDraftOrder(id: String):Flow<Response<T>> =
        remoteDataSource.removeDraftOrder(id)
    override suspend fun <T> getUserEmail(): Flow<Response<T>> {
        return remoteDataSource.getUserEmail()
    }
}