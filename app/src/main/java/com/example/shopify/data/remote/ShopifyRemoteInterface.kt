package com.example.shopify.data.remote

import com.example.shopify.auth.data.dto.CustomerResponse
import com.example.shopify.checkout.data.dto.DraftOrderById
import com.example.shopify.checkout.data.dto.discountcode.DiscountCodeResponse
import com.example.shopify.checkout.data.dto.product.OneProductResponse
import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.dto.codes.DiscountCodesResponse
import com.example.shopify.home.data.dto.BrandsResponse
import com.example.shopify.home.data.dto.ProductsResponse
import com.example.shopify.orders.data.dto.OrdersResponse
import com.example.shopify.productdetails.data.dto.draftorder.DraftOrderRequest
import com.example.shopify.productdetails.data.dto.productdetails.ProductDetailsResponse
import com.example.shopify.settings.data.dto.currencies.CurrenciesResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopifyRemoteInterface {

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
        @Query("product_type") productType: String = ""
    ): ProductsResponse


    @GET("products/{productId}.json")
    suspend fun getProductById(@Path("productId") productId : String): OneProductResponse




    @POST("customers.json")
    suspend fun createCustomer(@Body customerResponse: CustomerResponse): CustomerResponse

    @GET("draft_orders.json")
    suspend fun getDraftOrders(
        @Query("customer_id") id: String = "",
        @Query("note") note: String = ""
    ): DraftOrderResponse


    @GET("draft_orders/{draftOrderId}.json")
    suspend fun getDraftOrderById(@Path("draftOrderId") draftOrderId:String): DraftOrderById

    @DELETE("draft_orders/{draftOrderId}.json")
    suspend fun removeDraftOrder(@Path("draftOrderId") draftOrderId:String)


    @PUT("draft_orders/{draftOrderId}.json")
    suspend fun updateDraftOrder(@Path("draftOrderId") draftOrderId:String ,@Body draftOrder: DraftOrderById)

    
    @GET("orders.json")
    suspend fun getCustomerOrders(@Query("email") customerEmail: String): OrdersResponse

    @GET("price_rules/1396109508887/discount_codes.json")
    suspend fun getDiscountCodes() : DiscountCodesResponse?



    @GET("price_rules/1396109508887/discount_codes/{id}.json")
    suspend fun getDiscountCodeById(id : String) : DiscountCodeResponse?

    @GET("products/{productId}.json")
    suspend fun getProductDetailsById(@Path("productId") productId : String): ProductDetailsResponse


    @GET("draft_orders.json")
    suspend fun getDraftOrders(
        @Query("fields") fields: String = "",
    ): DraftOrderResponse

    @POST("draft_orders.json")
    suspend fun createDraftOrder(@Body draftOrderRequest: DraftOrderRequest):DraftOrderRequest


}