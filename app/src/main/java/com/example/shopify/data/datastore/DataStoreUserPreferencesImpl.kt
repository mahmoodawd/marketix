package com.example.shopify.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("preferences")

class DataStoreUserPreferencesImpl @Inject constructor(private val context: Context) :
    DataStoreUserPreferences {


    override suspend fun putString(key: String, value: String) {
        val preferenceKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it[preferenceKey] = value
        }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        val preferenceKey = booleanPreferencesKey(key)
        context.dataStore.edit {
            it[preferenceKey] = value
        }
    }

    override suspend fun <T>getString(key: String): Flow<Response<T>> {


            val preferenceKey = stringPreferencesKey(key)
             return context.dataStore.data
                .catch {  Response.Failure<T>( it.message ?: "error") }
                .map { preference ->

                  Response.Success(preference[preferenceKey] as T)
                }
    }

    override suspend fun <T> getBoolean(key: String):  Flow<Response<T>> {

        val preferenceKey = booleanPreferencesKey(key)
        return context.dataStore.data
            .catch {  Response.Failure<T>( it.message ?: "error") }
            .map { preference ->
                Response.Success(preference[preferenceKey] as T)
            }
    }
}