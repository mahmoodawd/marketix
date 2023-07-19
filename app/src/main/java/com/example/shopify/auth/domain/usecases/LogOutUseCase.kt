package com.example.shopify.auth.domain.usecases

import com.example.shopify.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val authRepository: AuthRepository) {
     operator fun invoke() = authRepository.logout()
}