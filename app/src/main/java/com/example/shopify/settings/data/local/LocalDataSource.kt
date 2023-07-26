package com.example.shopify.settings.data.local

import com.example.shopify.settings.data.dto.location.AddressDto
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun <T>  saveStringToDataStore(key : String , value : String) : Flow<Response<T>>

    suspend fun <T>  saveBooleanToDataStore(key : String , value : Boolean) : Flow<Response<T>>


    suspend fun <T> getBooleanFromDataStore(key : String) : Flow<Response<T>>

    suspend fun <T> getStringFromDataStore(key : String) : Flow<Response<T>>




}