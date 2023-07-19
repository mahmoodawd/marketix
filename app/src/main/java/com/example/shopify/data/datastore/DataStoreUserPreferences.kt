package com.example.shopify.data.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreUserPreferences {

    suspend fun putString(key : String , value : String)
    suspend fun putBoolean(key : String , value: Boolean)
    suspend fun <T> getString (key : String) : Flow<Response<T>>
    suspend fun getBoolean(key : String) : Boolean?


}