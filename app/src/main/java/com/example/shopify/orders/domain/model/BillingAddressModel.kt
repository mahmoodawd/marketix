package com.example.shopify.orders.domain.model

data class BillingAddressModel(
    val address1: String,
    val city: String,
    val country: String,
    val country_code: String,
    val first_name: String,
    val last_name: String,
    val name: String,
    val phone: String,
    val zip: String
)
