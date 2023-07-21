package com.example.shopify.auth.domain.repository

import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun <T> login(email: String, password: String): Flow<Response<T>>
    suspend fun <T> signup(name: String, email: String, password: String): Flow<Response<T>>
    suspend fun <T> resetPassword(email: String): Flow<Response<T>>
    fun logout()
    suspend fun <T> createCustomerAccount(user: FirebaseUser): Flow<Response<T>>
}