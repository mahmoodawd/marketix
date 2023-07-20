package com.example.shopify.home.domain.model


data class ProductModel(
    val handle: String,
    val id: Long,
    val image: ImageModel,
    val images: List<ImageModel>,
    val options: List<OptionModel>,
    val productType: String,
    val status: String,
    val tags: String,
    val title: String,
    val variants: List<VariantModel>,
    val vendor: String
)
