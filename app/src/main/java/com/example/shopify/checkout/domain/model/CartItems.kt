package com.example.shopify.checkout.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CartItems(val cartItems : List<CartItem>) : Parcelable