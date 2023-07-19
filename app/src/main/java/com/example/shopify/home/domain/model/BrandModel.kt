package com.example.shopify.home.domain.model

data class BrandModel(
    val id: Long,
    val title: String,
    val image: ImageModel,
    var clicked : Boolean = false
)


