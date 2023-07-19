package com.example.shopify.settings.data.remote

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun <T>  getAllCurrencies(): Flow<Response<T>>

    suspend fun <T> getAllCities() :  Flow<Response<T>>
}