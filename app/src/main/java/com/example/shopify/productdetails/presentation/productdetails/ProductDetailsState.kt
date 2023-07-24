package com.example.shopify.productdetails.presentation.productdetails

import com.example.shopify.productdetails.domain.model.details.ProductsDetailsModel

data class ProductDetailsState(

    val product: ProductsDetailsModel? = null,
    val loading: Boolean = true,
    val isFavorite: Boolean = false,
    val isCartItem: Boolean = false,

    )
