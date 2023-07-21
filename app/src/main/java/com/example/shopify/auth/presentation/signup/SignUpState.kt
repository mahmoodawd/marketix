package com.example.shopify.auth.presentation.signup

sealed interface SignUpState {
    data class Display(
        val success: Boolean? = null,
        val loading: Boolean? = null
    )
}