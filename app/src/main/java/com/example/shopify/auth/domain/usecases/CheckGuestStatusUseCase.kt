package com.example.shopify.auth.domain.usecases

import com.example.shopify.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class CheckGuestStatusUseCase @Inject constructor(private val authRepository: AuthRepository) {
    val currentUser: FirebaseUser?
        get() = authRepository.currentUser

    operator fun invoke(): Boolean = currentUser == null

}