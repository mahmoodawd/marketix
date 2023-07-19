package com.example.shopify.settings.domain.repository

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun <T>  saveStringToDataStore(key : String , value : String) : Flow<Response<T>>


    suspend fun <T> getStringFromDataStore(key : String) : Flow<Response<T>>


    suspend fun <T>  saveBooleanToDataStore(key : String , value : Boolean) : Flow<Response<T>>


    suspend fun <T> getBooleanFromDataStore(key : String) : Flow<Response<T>>



    suspend fun <T> getAllCurrencies() : Flow<Response<T>>


    suspend fun <T> getAllCities() :  Flow<Response<T>>

}