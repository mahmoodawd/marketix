package com.example.shopify.settings.data.dto.address

data class SendAddress(
    val address1: String = "",
    val address2: String = "",
    val city: String = "",
    val company: String = "",
    val first_name: String = "",
    val phone: String = "",
    val country: String = "",
    val zip: String = "",
    val name: String = "",
    val country_code: String = "eg",
    val country_name:String = "egypt",
)