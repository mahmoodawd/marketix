package com.example.shopify.data.remote

import com.example.shopify.BuildConfig
import com.example.shopify.home.data.dto.BrandsResponse
import com.example.shopify.settings.data.dto.currencies.CurrenciesResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface RemoteInterface {

    @GET("currencies.json")
    suspend fun getAllCurrencies():CurrenciesResponse

    @GET("smart_collections.json")
    suspend fun getAllBrands():BrandsResponse

}