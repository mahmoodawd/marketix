package com.example.shopify.favorites.presentation

import com.example.shopify.favorites.domain.model.FavoriteProductModel

data class FavoritesState(

    val products: List<FavoriteProductModel>? = emptyList(),
    val loading: Boolean = true,
    val empty: Boolean = true

)
