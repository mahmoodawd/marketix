package com.example.shopify.settings.domain.model

data class AddressModel(
    val addressId: String? = null,
    val latitude: Double, val longitude: Double, val city: String, val address: String,
)