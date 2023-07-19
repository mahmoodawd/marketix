package com.example.shopify.settings.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun <T>  saveStringToDataStore(key : String , value : String) : Flow<Response<T>>


    suspend fun <T> getStringFromDataStore(key : String) : Flow<Response<T>>


    suspend fun <T> getAllCurrencies() : Flow<Response<T>>

}