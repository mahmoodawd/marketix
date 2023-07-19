package com.example.shopify.data.remote

import com.example.shopify.BuildConfig
import com.example.shopify.settings.data.dto.CurrenciesResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface RemoteInterface {

    @GET("currencies.json")
    suspend fun getAllCurrencies():CurrenciesResponse

}