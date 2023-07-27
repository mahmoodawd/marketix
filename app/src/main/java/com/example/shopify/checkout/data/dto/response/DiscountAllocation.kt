package com.example.shopify.checkout.data.dto.response

data class DiscountAllocation(
    val amount: String,
    val amount_set: AmountSet,
    val discount_application_index: Int
)