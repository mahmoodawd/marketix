package com.example.shopify.orders.presentation

import com.example.shopify.orders.domain.model.OrderModel

data class OrdersState(
    val orders: List<OrderModel> = listOf(),
    val error: String? = null,
    val loading: Boolean? = null
)
