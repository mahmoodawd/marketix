package com.example.shopify.orders.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderModel(
    val contactEmail: String,
    val createdAt: String,
    val currentSubtotalPrice: String,
    val currentTotalDiscounts: String,
    val currentTotalPrice: String,
    val email: String,
    val id: Long,
    val lineItems: List<LineItemModel>,
    val name: String,
    val number: Int,
    val orderNumber: Int,
    val orderStatusUrl: String,
    val subtotalPrice: String,
    val tags: String,
    val test: Boolean,
    val token: String,
    val totalDiscounts: String,
    val totalLineItemsPrice: String,
    val totalOutstanding: String,
    val totalPrice: String,
    val totalWeight: Int,
    val updatedAt: String,
):Parcelable
