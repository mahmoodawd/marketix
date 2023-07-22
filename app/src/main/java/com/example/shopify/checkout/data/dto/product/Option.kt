package com.example.shopify.checkout.data.dto.product

data class Option(
    val id: Long,
    val name: String,
    val position: Int,
    val product_id: Long,
    val values: List<String>
)