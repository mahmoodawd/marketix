package com.example.shopify.settings.data.dto.currencies

data class Currency(
    val currency: String,
    val enabled: Boolean,
    val rate_updated_at: String
)