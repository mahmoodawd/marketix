package com.example.shopify.productdetails.data.dto.draftorder

data class LineItem(
    val variant_id: Long,
    val quantity: Int,
    val properties: List<PropertyItem>
)