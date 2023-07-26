package com.example.shopify.checkout.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PropertyModel(val imageUrl : String , val value : String) : Parcelable