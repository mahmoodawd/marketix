package com.example.shopify.auth.data.repository

import com.example.shopify.auth.domain.entities.AuthState
import com.example.shopify.auth.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepoImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): AuthState<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (result.user?.isEmailVerified!!) {
                AuthState.Success(result.user!!)
            } else {
                AuthState.UnVerified
            }
        } catch (e: Exception) {
            AuthState.Failure(e)
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): AuthState<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            result.user?.sendEmailVerification()?.await()
            AuthState.EmailSent
        } catch (e: Exception) {
            AuthState.Failure(e)
        }
    }

    override suspend fun resetPassword(email: String): AuthState<FirebaseUser> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            AuthState.EmailSent
        } catch (e: Exception) {
            AuthState.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}