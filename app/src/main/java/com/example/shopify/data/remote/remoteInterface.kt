package com.example.shopify.data.remote

import com.example.shopify.settings.data.dto.CurrenciesResponse
import com.example.shopify.utils.response.ApiResult
import retrofit2.Response
import retrofit2.http.GET

interface RemoteInterface {

    @GET("currencies.json")
    suspend fun getAllCurrencies():CurrenciesResponse

}