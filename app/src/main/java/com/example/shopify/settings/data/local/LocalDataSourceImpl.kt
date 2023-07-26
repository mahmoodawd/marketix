package com.example.shopify.settings.data.local

import com.example.shopify.data.datastore.DataStoreUserPreferences
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStoreUserPreferences,
    private val addressDao: AddressDao
) : LocalDataSource {
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

    override suspend fun <T> saveBooleanToDataStore(
        key: String,
        value: Boolean
    ): Flow<Response<T>> {
        return try {
            dataStore.putBoolean(key, value)
            flowOf(Response.Success("successful insert" as T))
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "unknown error"))
        }
    }

    override suspend fun <T> getBooleanFromDataStore(key: String): Flow<Response<T>> {
        return try {
            return dataStore.getBoolean(key)
        } catch (e: Exception) {
            flowOf(Response.Failure(e.message ?: "unknown error"))
        }
    }


}