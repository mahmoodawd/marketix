package com.example.shopify.productdetails.presentation.productdetails

import com.example.shopify.productdetails.domain.model.details.ProductsDetailsModel

data class ProductDetailsState(

    val product: ProductsDetailsModel? = null,
    val currencyFactor: Double = 1.0,
    val currency : String = "EGP",
    val loading: Boolean = true,
    val isFavorite: Boolean = false,
    val isCartItem: Boolean = false,

    )
