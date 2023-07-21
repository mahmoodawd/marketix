package com.example.shopify.auth.domain.usecases

import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun <T> invoke(email: String): Flow<Response<T>> =
        authRepository.resetPassword(email = email)
}