package com.example.shopify.favorites.data.remote

import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.remote.ShopifyRemoteInterface
import timber.log.Timber
import javax.inject.Inject

class FavoriteProductsRemoteDataSource @Inject constructor(
    private val remoteInterface: ShopifyRemoteInterface
) :
    RemoteDataSource {

    override suspend fun getFavoritesProducts(): DraftOrderResponse {

        val orders = remoteInterface.getDraftOrders()
        Timber.i("Fav Data Source: Done ->> ${orders.draft_orders.size}")
        return orders

    }

    override suspend fun removeDraftOrder(id: String) {
        remoteInterface.removeDraftOrder(id)
    }
}