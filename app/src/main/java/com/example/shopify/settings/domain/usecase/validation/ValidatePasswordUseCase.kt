package com.example.shopify.settings.domain.usecase.validation

class ValidatePasswordUseCase {

    fun execute(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$".toRegex()
        return passwordPattern.matches(password)
    }

}