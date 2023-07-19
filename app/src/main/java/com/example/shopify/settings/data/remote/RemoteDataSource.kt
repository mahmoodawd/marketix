package com.example.shopify.settings.data.remote

import com.example.shopify.settings.data.dto.CurrenciesResponse
import com.example.shopify.utils.response.ApiResult
import com.example.shopify.utils.response.Response
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun <T>  getAllCurrencies(): Flow<Response<T>>
}