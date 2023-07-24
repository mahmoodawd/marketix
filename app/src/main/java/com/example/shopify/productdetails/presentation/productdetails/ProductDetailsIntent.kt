package com.example.shopify.productdetails.presentation.productdetails

sealed interface ProductDetailsIntent {

    data class GetDetails(val productId: String) : ProductDetailsIntent

}