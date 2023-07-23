package com.example.shopify.orders.domain.model

data class CustomerModel(
    val createdAt: String,
    val currency: String,
    val email: String,
    val firstName: String?,
    val id: Long,
    val lastName: String?,
    val note: Any?,
    val phone: String?,
    val tags: String,
    val updatedAt: String,
    val verifiedEmail: Boolean
)
