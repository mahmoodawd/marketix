package com.example.shopify.home.presentation

import com.example.shopify.home.domain.model.BrandModel

sealed interface HomeState {
    data class Display(
        val brands: List<BrandModel> = listOf(),
        val error: String? = null,
        val loading: Boolean? = null
    ) : HomeState
}