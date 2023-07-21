package com.example.shopify.settings.data.dto.location

data class CitiesResponse(
    val `data`: List<String>,
    val error: Boolean,
    val msg: String
)