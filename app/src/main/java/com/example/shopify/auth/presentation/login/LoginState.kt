package com.example.shopify.auth.presentation.login

sealed interface LoginState {
    data class Display(
        val success: Boolean? = null,
        val unVerified: Boolean? = null,
        val loading: Boolean? = null
    )
}