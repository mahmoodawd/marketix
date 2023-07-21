package com.example.shopify.auth.data.repository

import com.example.shopify.auth.data.remote.RemoteDataSource
import com.example.shopify.auth.domain.entities.AuthState
import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource, private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun <T> createCustomerAccount(user: FirebaseUser): Flow<Response<T>> = flowOf(
        try {
            Timber.i("Is User Verified: ${user.isEmailVerified}")
            Response.Success(
                remoteDataSource.createNewCustomer(user) as T
            )
        } catch (e: Exception) {
            Response.Failure(e.message ?: "UnKnown Exception")
        }
    )

    override suspend fun <T> login(email: String, password: String): Flow<Response<T>> = flowOf(
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (result.user?.isEmailVerified!!) {
                Response.Success(result.user as T)
            } else {
                Response.Failure("emailNotVerifiedException")
            }
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownException")
        }
    )

    override suspend fun <T> signup(
        name: String,
        email: String,
        password: String
    ): Flow<Response<T>> = flowOf(
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            result.user?.sendEmailVerification()?.await()
            Response.Success(result.user?.email as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownException")
        }
    )


    override suspend fun <T>resetPassword(email: String): Flow<Response<T>> = flowOf(

       try {
             firebaseAuth.sendPasswordResetEmail(email).await()
            Response.Success(email as T)
        } catch (e: Exception) {
           Response.Failure(e.message ?: "unknownException")
    }

    )

    override fun logout() {
        firebaseAuth.signOut()
    }
}