package com.example.shopify.orders.data.dto

import com.example.shopify.data.dto.PropertiesItem

data class LineItem(
    val id: Long = -1,
    val name: String = "",
    val price: String = "",
    val product_id: Long = -1,
    val properties: List<PropertiesItem>? = listOf(),
    val quantity: Int = -1,
    val title: String = "",
    val variant_id: Long = -1,
    val variant_title: String = "",
    val vendor: String = ""
)