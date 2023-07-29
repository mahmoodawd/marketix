package com.example.shopify.auth.data.repository

import com.example.shopify.auth.data.mappers.toCustomer
import com.example.shopify.auth.data.remote.RemoteDataSource
import com.example.shopify.auth.domain.UserModel
import com.example.shopify.auth.domain.repository.AuthRepository
import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore

) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun <T> createCustomerAccount(user: FirebaseUser): Flow<Response<T>> = flowOf(
        try {
            Timber.i("Is User Verified: ${user.isEmailVerified}")
            Response.Success(
                remoteDataSource.createNewCustomer(user.toCustomer()) as T
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
        userModel: UserModel
    ): Flow<Response<T>> = flowOf(
        try {
            val result =
                firebaseAuth.createUserWithEmailAndPassword(userModel.email, userModel.password)
                    .await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(userModel.userName)
                    .build()
            )?.await()

            firebaseFirestore.collection("users").document(result!!.user!!.uid)
                .set(mapOf(result.user!!.uid to userModel.phone)).await()

            result.user?.sendEmailVerification()?.await()
            Response.Success(result.user?.email as T)
        } catch (e: Exception) {
            Response.Failure(e.message ?: "unknownException")
        }
    )


    override suspend fun <T> resetPassword(email: String): Flow<Response<T>> = flowOf(

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