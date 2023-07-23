package com.example.shopify.productdetails.domain.model


data class ProductsDetailsModel(
    val id: Long,
    val title: String,
    val vendor: String,
    val description: String,
    val rating: Float,
    val images: List<ImageModel>,
    val productType: String,
    val handle: String,
    val status: String,
    val tags: String,
    val options: List<OptionModel>?,
    val variants: List<VariantModel>?,
)
