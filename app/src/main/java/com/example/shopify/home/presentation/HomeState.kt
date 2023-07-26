package com.example.shopify.home.presentation

import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.home.domain.model.BrandModel
import com.example.shopify.home.domain.model.ProductModel
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel

sealed interface HomeState {
    data class Display(
        val brands: List<BrandModel> = listOf(),
        val products: List<ProductModel> = listOf(),
        val discountCode : DiscountCodeModel? = null,
        val error: String? = null,
        val loading: Boolean? = null,
        val currency: String = "EGP",
        val exchangeRate :Double = 1.0
    ) : HomeState
}