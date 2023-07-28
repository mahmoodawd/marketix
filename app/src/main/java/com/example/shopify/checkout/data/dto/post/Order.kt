package com.example.shopify.checkout.data.dto.post


data class Order(
    val email: String,
    val line_items: List<LineItem>,
    val discount_codes: List<DiscountCode>,
    val send_receipt: Boolean = true,
    val inventory_behaviour: String = "decrement_obeying_policy",
    )