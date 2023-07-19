package com.example.shopify.settings.data.local

import com.example.shopify.data.datastore.DataStoreUserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val dataStore: DataStoreUserPreferences,): LocalDataSource {
    override suspend fun <T> saveStringToDataStore(key: String, value: String): Flow<Response<T>> {



        return try {
            dataStore.putString(key, value)
            flowOf(Response.Success("successful insert" as T))
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "unknown error"))
        }
    }

    override suspend fun <T> getStringFromDataStore(key: String): Flow<Response<T>> {
        return try {
            return dataStore.getString(key)
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "unknown error"))
        }
    }
}