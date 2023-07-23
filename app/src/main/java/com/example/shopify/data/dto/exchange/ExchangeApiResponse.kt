package com.example.shopify.data.dto.exchange

data class ExchangeApiResponse(
    val date: String,
    val info: Info,
    val query: Query,
    val result: Double,
    val success: Boolean
)