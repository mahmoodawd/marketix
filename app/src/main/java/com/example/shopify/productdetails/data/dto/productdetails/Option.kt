package com.example.shopify.productdetails.data.dto.productdetails

data class Option(
    val id: Long,
    val name: String,
    val position: Int,
    val product_id: Long,
    val values: List<String>
)