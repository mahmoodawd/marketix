package com.example.shopify.orders.domain.model

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
     val properties: List<Any>,
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