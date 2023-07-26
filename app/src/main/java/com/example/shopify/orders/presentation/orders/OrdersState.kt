package com.example.shopify.orders.presentation.orders

import com.example.shopify.orders.domain.model.OrderModel

data class OrdersState(
    val orders: List<OrderModel> = listOf(),
    val error: String? = null,
    val loading: Boolean? = null,
    val currency: String = "EGP",
    val exchangeRate :Double = 1.0
)
