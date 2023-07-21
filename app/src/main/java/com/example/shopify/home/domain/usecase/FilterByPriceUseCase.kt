package com.example.shopify.home.domain.usecase

import com.example.shopify.home.domain.model.ProductModel

class FilterByPriceUseCase {
    operator fun invoke(
        max: Double,
        min: Double,
        products: List<ProductModel>
    ): List<ProductModel> {
        return products.filter { it.variants!![0].price.toDouble() < max && it.variants[0].price.toDouble() > min }
    }
}