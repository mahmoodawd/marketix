package com.example.shopify.settings.domain.usecase.validation

import java.util.regex.Pattern

class ValidateUserNameUseCase {


    fun execute(userName : String ) : Boolean
    {
        val regex = "^[a-zA-Z][a-zA-Z0-9_]{7,30}$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(userName)
        return matcher.matches()
    }
}