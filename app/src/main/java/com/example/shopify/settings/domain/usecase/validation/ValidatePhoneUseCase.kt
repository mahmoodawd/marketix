package com.example.shopify.settings.domain.usecase.validation

import java.util.regex.Pattern

class ValidatePhoneUseCase {

    fun execute(phoneNumber: String): Boolean {
        val regex = "^[0-9]{10,13}$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(phoneNumber)
        return matcher.matches()
    }
}