package com.example.shopify.settings.data.remote

import com.example.shopify.settings.data.dto.location.CitiesPostRequest
import com.example.shopify.settings.data.dto.location.CitiesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RemoteCountriesInterface {


    @POST("countries/cities")
    suspend fun getAllCities(@Body citiesPostRequest: CitiesPostRequest) : CitiesResponse
}