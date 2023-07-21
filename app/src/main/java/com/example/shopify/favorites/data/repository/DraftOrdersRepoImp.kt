package com.example.shopify.favorites.data.repository

import com.example.shopify.domain.repository.DraftOrdersRepository
import com.example.shopify.favorites.data.local.LocalDataSource
import com.example.shopify.favorites.data.mappers.toFavoritesModel
import com.example.shopify.favorites.data.remote.RemoteDataSource
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class DraftOrdersRepoImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : DraftOrdersRepository {

    override suspend fun <T> getDraftOrders(): Flow<Response<T>> = flowOf(
        try {
            //should be mapped
            Response.Success(remoteDataSource.getFavoritesProducts().toFavoritesModel() as T)
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