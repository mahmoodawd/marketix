package com.example.shopify.orders.data.dto

data class DiscountApplication(
    val allocation_method: String,
    val description: String,
    val target_selection: String,
    val target_type: String,
    val title: String,
    val type: String,
    val value: String,
    val value_type: String
)
