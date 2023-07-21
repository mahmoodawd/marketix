package com.example.shopify.auth.data.mappers

import com.example.shopify.auth.data.dto.CustomerResponse
import com.example.shopify.auth.data.dto.CustomerResponseInfo
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toCustomer() = CustomerResponse(
    CustomerResponseInfo(
        first_name = displayName,
        email = email,
        verified_email = isEmailVerified,
    )
)
