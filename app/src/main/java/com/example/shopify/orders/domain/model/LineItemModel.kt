package com.example.shopify.orders.domain.model

import android.os.Parcelable
import com.example.shopify.data.dto.PropertiesItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class LineItemModel(
     val id: Long,
     val name: String,
     val price: String,
     val productId: Long,
     val properties: List<PropertiesItem>,
     val quantity: Int,
     val title: String,
     val variantId: Long,
     val variantTitle: String,
     val vendor: String
 ):Parcelable