package com.example.shopify.settings.data.dto

data class Currency(
    val currency: String,
    val enabled: Boolean,
    val rate_updated_at: String
)