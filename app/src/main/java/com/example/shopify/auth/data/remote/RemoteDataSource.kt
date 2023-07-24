package com.example.shopify.auth.data.remote

import com.example.shopify.auth.data.dto.CustomerResponse

interface RemoteDataSource {
    suspend fun <T> createNewCustomer(customerResponse: CustomerResponse): T
}