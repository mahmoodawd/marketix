package com.example.shopify.favorites.presentation

import com.example.shopify.favorites.domain.model.FavoriteProductModel

sealed interface FavoritesIntent {

    object GetFavorites : FavoritesIntent

    data class RemoveFromFavorites(val product: FavoriteProductModel) : FavoritesIntent

}