package com.example.shopify.checkout.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val itemId: Long,
    val itemName: String,
    var subtotalPrice: String,
    var totalTax : String,
    var total : String,
    var currency: String,
    val imageUrl: String,
    var quantity: String,
    val upperLimit : Int,
    val oneItemPrice : String,
    val oneItemTax : String,
    val variantId : String,
) : Parcelable