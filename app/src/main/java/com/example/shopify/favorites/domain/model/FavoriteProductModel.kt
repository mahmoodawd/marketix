package com.example.shopify.favorites.domain.model

data class FavoriteProductModel(
    var draftOrderId: Long,
    val id: Long,
    var imageSrc: String,
    val title: String,
    val price: String,
    var currency: String,
    val vendor: String

)