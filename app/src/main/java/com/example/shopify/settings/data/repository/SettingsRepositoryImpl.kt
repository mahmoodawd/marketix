package com.example.shopify.settings.data.repository

import com.example.shopify.settings.data.dto.CurrenciesResponse
import com.example.shopify.settings.data.local.LocalDataSource
import com.example.shopify.settings.data.mappers.toCurrenciesModel
import com.example.shopify.settings.data.remote.RemoteDataSource
import com.example.shopify.settings.domain.repository.SettingsRepository
import com.example.shopify.utils.response.ApiResult
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource) : SettingsRepository {

    override suspend fun <T> saveStringToDataStore(key: String, value: String): Flow<Response<T>> {
        return localDataSource.saveStringToDataStore(key, value)
    }

    override suspend fun <T> getStringFromDataStore(key: String): Flow<Response<T>> {
        return localDataSource.getStringFromDataStore(key)
    }

    override suspend fun <T> getAllCurrencies(): Flow<Response<T>> {
        return remoteDataSource.getAllCurrencies()
    }
}