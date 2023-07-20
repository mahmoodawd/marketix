package com.example.shopify.data.remote

import com.example.shopify.BuildConfig
import com.example.shopify.home.data.dto.BrandsResponse
import com.example.shopify.home.data.dto.ProductsResponse
import com.example.shopify.settings.data.dto.currencies.CurrenciesResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteInterface {

    @GET("currencies.json")
    suspend fun getAllCurrencies():CurrenciesResponse

    @GET("smart_collections.json")
    suspend fun getAllBrands():BrandsResponse

    @GET("products.json")
    suspend fun getAllProducts(@Query("vendor") vendor: String,@Query("id") id: Long):ProductsResponse

    @GET("collections/{collection_id}/products.json")
    suspend fun getCategoryProducts(@Path("collection_id") collectionId: Long): ProductsResponse

}