package com.example.shopify.orders.data.dto

data class Order(
    val contact_email: String="",
    val created_at: String="",
    val current_total_price: String="",
    val email: String="",
    val subtotal_price: String="",
    val id: Long = -1,
    val line_items: List<LineItem> = listOf(),
    val name: String="",
    val number: Int = -1,
    val order_number: Int = -1,
    val tags: String="",
    val test: Boolean = false,
    val total_line_items_price: String="",
    val total_price: String="",
)