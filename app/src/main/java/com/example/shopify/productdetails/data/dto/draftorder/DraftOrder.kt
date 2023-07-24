package com.example.shopify.productdetails.data.dto.draftorder

data class DraftOrder(
    val email: String,
    val tags: String,
    val line_items: List<LineItem>
)




