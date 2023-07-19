package com.example.shopify.auth.domain.usecases

import com.google.firebase.auth.FirebaseUser

data class AuthenticationUseCase (
    val signInUseCase: SignInUseCase,
    val signUpUseCase: SignUpUseCase,
    val logOutUseCase: LogOutUseCase,
    val currentUser: FirebaseUser?
)