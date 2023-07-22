package com.example.shopify.favorites.data.remote

import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.remote.RemoteInterface
import javax.inject.Inject

class FavoriteProductsRemoteDataSource @Inject constructor(private val remoteInterface: RemoteInterface) :
    RemoteDataSource {

    override suspend fun getFavoritesProducts(): DraftOrderResponse =
        remoteInterface.getDraftOrders()  //request only fav by passing note parameter

    override suspend fun removeDraftOrder(id: String) {
        remoteInterface.removeDraftOrder(id)
    }
}