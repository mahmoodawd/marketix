package com.example.shopify.home.presentation

import com.example.shopify.home.domain.model.BrandModel
import com.example.shopify.home.domain.model.ProductModel
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.search.domain.model.SearchProductModel

sealed interface HomeState {
    data class Display(
        val brands: List<BrandModel> = listOf(),
        val products: List<ProductModel> = listOf(),
        val discountCode: DiscountCodeModel? = null,
        val error: String? = null,
        val loading: Boolean = true,
        val currency: String = "EGP",
        val exchangeRate: Double = 1.0,
        val searchResult: List<SearchProductModel> = emptyList()
    ) : HomeState
}