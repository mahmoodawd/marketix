package com.example.shopify.checkout.presentation.cart

sealed interface CartIntent
{
    object GetAllCartItems : CartIntent
}