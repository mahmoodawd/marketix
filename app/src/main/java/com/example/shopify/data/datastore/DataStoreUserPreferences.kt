package com.example.shopify.data.datastore

import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface DataStoreUserPreferences {

    suspend fun putString(key : String , value : String)
    suspend fun putBoolean(key : String , value: Boolean)
    suspend fun <T> getString (key : String) : Flow<Response<T>>
    suspend fun <T>getBoolean(key : String) : Flow<Response<T>>


}