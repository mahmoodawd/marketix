package com.example.shopify.favorites.data.remote

import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.remote.ShopifyRemoteInterface
import javax.inject.Inject

class FavoriteProductsRemoteDataSource @Inject constructor(private val remoteInterface: ShopifyRemoteInterface) :
    RemoteDataSource {

    override suspend fun getFavoritesProducts(): DraftOrderResponse =
        remoteInterface.getDraftOrders()

    override suspend fun removeDraftOrder(id: String) {
        remoteInterface.removeDraftOrder(id)
    }
}