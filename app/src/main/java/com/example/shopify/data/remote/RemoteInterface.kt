package com.example.shopify.data.remote

import com.example.shopify.BuildConfig
import com.example.shopify.home.data.dto.BrandsResponse
import com.example.shopify.home.data.dto.ProductsResponse
import com.example.shopify.settings.data.dto.currencies.CurrenciesResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RemoteInterface {

    @GET("currencies.json")
    suspend fun getAllCurrencies(): CurrenciesResponse

    @GET("smart_collections.json")
    suspend fun getAllBrands(): BrandsResponse

    @GET("products.json")
    suspend fun getAllProducts(): ProductsResponse

    @GET("products.json")
    suspend fun getProductsByBrand(@Query("vendor") vendor: String): ProductsResponse

    @GET("products.json")
    suspend fun filterProducts(
        @Query("collection_id") collectionId: Long?,
        @Query("product_type") productType: String=""
    ): ProductsResponse


}