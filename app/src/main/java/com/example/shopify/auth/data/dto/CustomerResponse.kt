package com.example.shopify.auth.data.dto


data class CustomerResponse(
    val customer: CustomerResponseInfo
)

data class CustomerResponseInfo(
    var id: Long? = null,
    var first_name: String? = "No_Name",
    var last_name: String? = null,
    val email: String?,
    val password: String? = null,
    var verified_email: Boolean? = false,
    var password_confirmation: String? = null
)
