package com.example.shopify.productdetails.domain.model

data class VariantModel(
    val fulfillmentService: String,
    val grams: Int,
    val id: Long,
    val inventoryItemId: Long,
    val inventory_management: String,
    val inventoryPolicy: String,
    val inventoryQuantity: Int,
    val oldInventoryQuantity: Int,
    val option1: String?,
    val option2: String?,
    val option3: String?,
    val position: Int,
    val price: String,
    val productId: Long,
    val sku: String,
    val taxable: Boolean,
    val title: String,
    val weight: Int,
    val weightUnit: String
)
