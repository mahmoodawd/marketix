package com.example.shopify.checkout.presentation.cart

import com.example.shopify.checkout.domain.model.CartItem

data class CartState(val cartItems : List<CartItem> = emptyList() , val loading : Boolean = true)