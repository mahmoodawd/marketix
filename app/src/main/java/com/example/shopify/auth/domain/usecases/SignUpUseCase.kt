package com.example.shopify.auth.domain.usecases

import com.example.shopify.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(userName: String, email: String, password: String) =
        authRepository.signup(userName, email, password)
}