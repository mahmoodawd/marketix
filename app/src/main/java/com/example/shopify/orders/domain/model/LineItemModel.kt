package com.example.shopify.orders.domain.model

import com.example.shopify.data.dto.PropertiesItem

data class LineItemModel(
     val fulfillableQuantity: Int,
     val fulfillmentService: String,
     val giftCard: Boolean,
     val grams: Int,
     val id: Long,
     val name: String,
     val price: String,
     val productExists: Boolean,
     val productId: Long,
     val properties: List<PropertiesItem>,
     val quantity: Int,
     val sku: String,
     val taxable: Boolean,
     val title: String,
     val totalDiscount: String,
     val variantId: Long,
     val variantInventoryManagement: String,
     val variantTitle: String,
     val vendor: String
 )