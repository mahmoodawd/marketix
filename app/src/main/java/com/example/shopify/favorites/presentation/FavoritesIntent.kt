package com.example.shopify.favorites.presentation

sealed interface FavoritesIntent {

    object GetFavorites : FavoritesIntent

    data class RemoveFromFavorites(val id: String, val itemPosition: Int) : FavoritesIntent

    data class Search(val keyword: String) : FavoritesIntent

}