package com.example.shopify.checkout.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LineItemModel(val quantity : Int , val variantId : Long , val properties : List<PropertyModel>,val price : String) : Parcelable