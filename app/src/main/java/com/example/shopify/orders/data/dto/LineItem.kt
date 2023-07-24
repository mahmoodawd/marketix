package com.example.shopify.orders.data.dto

import com.example.shopify.data.dto.PropertiesItem

data class LineItem(
    val admin_graphql_api_id: String,
    val discount_allocations: List<String>,
    val duties: List<String>,
    val fulfillable_quantity: Int,
    val fulfillment_service: String,
    val fulfillment_status: String?,
    val gift_card: Boolean,
    val grams: Int,
    val id: Long,
    val name: String,
    val price: String,
    val product_exists: Boolean,
    val product_id: Long,
    val properties: List<PropertiesItem>?,
    val quantity: Int,
    val requires_shipping: Boolean,
    val sku: String,
    val tax_lines: List<String>,
    val taxable: Boolean,
    val title: String,
    val total_discount: String,
    val variant_id: Long,
    val variant_inventory_management: String?,
    val variant_title: String,
    val vendor: String
)