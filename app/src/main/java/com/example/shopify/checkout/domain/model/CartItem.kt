package com.example.shopify.checkout.domain.model

data class CartItem(
    val itemId: Long,
    val itemName: String,
    val itemPrice: String,
    val currency: String,
    val imageUrl: String,
    val quantity: String
)