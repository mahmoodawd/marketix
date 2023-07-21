package com.example.shopify.auth.domain.usecases

class ValidatePasswordUseCase {
    operator fun invoke(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$".toRegex()
        return passwordPattern.matches(password)
    }

}