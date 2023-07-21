package com.example.shopify.home.presentation

import com.example.shopify.home.domain.model.BrandModel
import com.example.shopify.home.domain.model.ProductModel

sealed interface HomeState {
    data class Display(
        val brands: List<BrandModel> = listOf(),
        val products: List<ProductModel> = listOf(),
        val error: String? = null,
        val loading: Boolean? = null
    ) : HomeState
}