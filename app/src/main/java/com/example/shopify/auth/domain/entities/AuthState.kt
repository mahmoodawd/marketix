package com.example.shopify.auth.domain.entities

import java.lang.Exception

sealed class AuthState<out G> {
    data class Success<out G>(val result: G) : AuthState<G>()
    data class Failure(val exception: Exception) : AuthState<Nothing>()
    object Loading : AuthState<Nothing>()
    object UnVerified : AuthState<Nothing>()
    object EmailSent : AuthState<Nothing>()

}