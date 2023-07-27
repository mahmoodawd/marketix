package com.example.shopify.orders.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderModel(
    val contactEmail: String,
    val createdAt: String,
    val currentTotalPrice: String,
    val email: String,
    val id: Long,
    val lineItems: List<LineItemModel>,
    val name: String,
    val number: Int,
    val orderNumber: Int,
    val subtotalPrice: String,
    val tags: String,
    val test: Boolean,
    val totalLineItemsPrice: String,
    val totalPrice: String,
):Parcelable
