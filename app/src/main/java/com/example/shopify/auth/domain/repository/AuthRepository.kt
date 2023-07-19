package com.example.shopify.auth.domain.repository

import com.example.shopify.auth.domain.entities.AuthState
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): AuthState<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): AuthState<FirebaseUser>
    fun logout()

}