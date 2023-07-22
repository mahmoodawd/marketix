package com.example.shopify.productdetails.domain.model


data class ProductsDetailsModel(
    val handle: String,
    val id: Long,
    val images: List<ImageModel>,
    val options: List<OptionModel>?,
    val productType: String,
    val status: String,
    val tags: String,
    val title: String,
    val variants: List<VariantModel>?,
    val vendor: String
)
