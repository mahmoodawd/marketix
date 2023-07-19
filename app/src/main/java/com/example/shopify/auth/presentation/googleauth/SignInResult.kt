package com.example.shopify.auth.presentation.googleauth

data class SignInResult(val data: UserData?, val errorMessage: String?)
data class UserData(val userId: String, val userName: String?, val avatarUrl: String?)