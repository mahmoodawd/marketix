package com.example.shopify.checkout.presentation.cart

import com.example.shopify.checkout.domain.model.CartItem

data class CartState(
    val cartItems: List<CartItem> = emptyList(),
    val currencyFactor: Double = 1.0,
    val cartTotalCost: Double = 0.0,
    val currency: String = "EGP",
    val loading: Boolean = true
)