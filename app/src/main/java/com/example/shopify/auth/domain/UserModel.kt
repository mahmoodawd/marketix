package com.example.shopify.auth.domain

data class UserModel(
    val userName: String,
    val phone: String,
    val email: String,
    val password: String,
)
