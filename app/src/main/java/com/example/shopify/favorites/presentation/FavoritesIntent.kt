package com.example.shopify.favorites.presentation

sealed interface FavoritesIntent {

    object GetFavorites : FavoritesIntent

    data class RemoveFromFavorites(val id: String) : FavoritesIntent

}