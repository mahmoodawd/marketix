package com.example.shopify.checkout.domain.model

data class CartItem(
    val itemId: Long,
    val itemName: String,
    var itemPrice: String,
    var currency: String,
    val imageUrl: String,
    var quantity: String,
    val upperLimit : Int,
    val oneItemPrice : String,
)