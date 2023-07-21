package com.example.shopify.auth.data.remote

import com.example.shopify.utils.response.Response
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun <T> createNewCustomer(user: FirebaseUser): T
}