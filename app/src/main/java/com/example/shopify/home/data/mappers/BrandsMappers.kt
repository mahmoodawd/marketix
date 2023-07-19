package com.example.shopify.home.data.mappers

import com.example.shopify.home.data.dto.BrandsResponse
import com.example.shopify.home.data.dto.Image
import com.example.shopify.home.data.dto.SmartCollection
import com.example.shopify.home.domain.model.BrandModel
import com.example.shopify.home.domain.model.BrandsModel
import com.example.shopify.home.domain.model.ImageModel

fun SmartCollection.toBrandModel(): BrandModel =
    BrandModel(id = this.id, title = this.title, image = this.image.toImageModel())


fun BrandsResponse.toBrandsModel(): BrandsModel =
    BrandsModel(brands = smart_collections.map { it.toBrandModel() })


fun Image.toImageModel(): ImageModel =
    ImageModel(src = this.src)
