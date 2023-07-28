package com.example.shopify.productdetails.presentation.productdetails

import com.example.shopify.productdetails.domain.model.details.ProductsDetailsModel
import com.example.shopify.productdetails.domain.model.details.VariantModel

sealed interface ProductDetailsIntent {

    data class GetDetails(val productId: String) : ProductDetailsIntent
    data class AddToFavorite(val product: ProductsDetailsModel) : ProductDetailsIntent
    data class AddToCart(val variant: VariantModel?, val product: ProductsDetailsModel) :
        ProductDetailsIntent


}