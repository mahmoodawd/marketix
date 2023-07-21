package com.example.shopify.auth.domain.usecases

import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateCustomerAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun <T> invoke(user: FirebaseUser): Flow<Response<T>> =
        authRepository.createCustomerAccount(user)
}