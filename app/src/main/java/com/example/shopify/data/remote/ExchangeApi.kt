package com.example.shopify.data.remote

import com.example.shopify.BuildConfig
import com.example.shopify.data.dto.exchange.ExchangeApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeApi {

    @GET("convert")
    suspend fun getExchangeMagicNumber(
        @Query("apikey") apikey: String = BuildConfig.EXCHANGE_TOKEN,
        @Query("amount") amount: String = "1",
        @Query("from") from: String = "EGP",
        @Query("to") to: String) : ExchangeApiResponse

}