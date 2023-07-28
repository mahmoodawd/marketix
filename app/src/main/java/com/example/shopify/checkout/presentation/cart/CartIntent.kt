package com.example.shopify.checkout.presentation.cart

sealed interface CartIntent
{
    data object GetAllCartItems : CartIntent

    data object CheckUserIsGuest : CartIntent
    data class DeleteCartItem(val id : String,val itemPosition : Int) : CartIntent

    data class UpdateCartItem(val id : String , val quantity : String ,val itemPosition: Int) : CartIntent
}