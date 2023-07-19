package com.example.shopify.auth.domain.usecases

import com.example.shopify.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class LogInUseCase @Inject constructor(private val authRepository: AuthRepository) {
     val currentUser: FirebaseUser?
        get() = authRepository.currentUser
    suspend operator fun invoke(email: String, password: String) =
        authRepository.login(email, password)
}