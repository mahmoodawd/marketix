package com.example.shopify.productdetails.presentation.productdetails

import com.example.shopify.productdetails.domain.model.ProductsDetailsModel

sealed interface ProductDetailsIntent {

    data class GetDetails(val productId: String) : ProductDetailsIntent
    data class AddToFavorite(val product: ProductsDetailsModel) : ProductDetailsIntent
    data class AddToCart(val product: ProductsDetailsModel) : ProductDetailsIntent


}