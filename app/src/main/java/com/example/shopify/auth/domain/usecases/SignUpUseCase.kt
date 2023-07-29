package com.example.shopify.auth.domain.usecases

import com.example.shopify.auth.domain.UserModel
import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun <T> invoke(
        userModel: UserModel,
    ): Flow<Response<T>> =
        authRepository.signup(userModel)
}