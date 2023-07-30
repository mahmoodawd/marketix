package com.example.shopify.orders.data.repository

import com.example.shopify.auth.domain.UserModel
import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class FakeAuthRepository(private val mockFirebaseUser: FirebaseUser) : AuthRepository {


    override val currentUser: FirebaseUser?
        get() = mockFirebaseUser

    override suspend fun <T> login(email: String, password: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> signup(userModel: UserModel): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override suspend fun <T> resetPassword(email: String): Flow<Response<T>> {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun <T> createCustomerAccount(user: FirebaseUser): Flow<Response<T>> {
        TODO("Not yet implemented")
    }
}