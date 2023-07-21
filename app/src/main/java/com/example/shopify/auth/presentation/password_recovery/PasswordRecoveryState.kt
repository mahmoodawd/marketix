package com.example.shopify.auth.presentation.password_recovery

sealed interface PasswordRecoveryState {
    data class Display(
        val success: Boolean? = null,
        val loading: Boolean? = null
    )
}