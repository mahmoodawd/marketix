package com.example.shopify.search.data.mappers

import com.example.shopify.search.data.dto.Product
import com.example.shopify.search.data.dto.SearchProductsResponse
import com.example.shopify.search.domain.model.SearchProductModel
import com.example.shopify.search.domain.model.SearchProductsModel

fun SearchProductsResponse.toSearchProductsModel() =
    SearchProductsModel(products.map { it.toSearchProductModel() })

fun Product.toSearchProductModel() = SearchProductModel(
    id = id.toString(),
    title = title,
    imageSrc = image.src
)
