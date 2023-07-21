package com.example.shopify.auth.domain.usecases

import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogInUseCase @Inject constructor(private val authRepository: AuthRepository) {
    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    suspend operator fun <T> invoke(email: String, password: String): Flow<Response<T>> =
        authRepository.login(email, password)
}