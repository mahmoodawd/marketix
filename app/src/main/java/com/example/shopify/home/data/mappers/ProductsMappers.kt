package com.example.shopify.home.data.mappers

import com.example.shopify.home.data.dto.ImageX
import com.example.shopify.home.data.dto.Option
import com.example.shopify.home.data.dto.Product
import com.example.shopify.home.data.dto.ProductsResponse
import com.example.shopify.home.data.dto.Variant
import com.example.shopify.home.domain.model.ImageModel
import com.example.shopify.home.domain.model.OptionModel
import com.example.shopify.home.domain.model.ProductModel
import com.example.shopify.home.domain.model.ProductsModel
import com.example.shopify.home.domain.model.VariantModel

fun Product.toProductModel(): ProductModel =
    ProductModel(
        handle = this.handle,
        id = this.id,
        image = this.image.toImageModel(),
        images = this.images.toImageListModel(),
        options = this.options.toOptionListModel(),
        productType = this.product_type,
        status = this.status,
        tags = this.tags,
        title = this.title,
        variants = this.variants?.toVariantListModel(),
        vendor = this.vendor
    )

fun ImageX.toImageModel(): ImageModel =
    ImageModel(src = this.src)

fun List<ImageX>.toImageListModel(): List<ImageModel> =
    (this.map { it.toImageModel() })

fun Option.toOptionModel(): OptionModel =
    OptionModel(
        id = this.id,
        name = this.name,
        position = this.position,
        productId = this.product_id,
        values = this.values
    )

fun List<Option>.toOptionListModel(): List<OptionModel> =
    (this.map { it.toOptionModel() })

fun Variant.toVariantModel(): VariantModel =
    VariantModel(
        fulfillmentService = fulfillment_service,
        grams = grams,
        id = id,
        inventoryItemId = inventory_item_id,
        inventory_management = inventory_management,
        inventoryPolicy = inventory_policy,
        inventoryQuantity = inventory_quantity,
        oldInventoryQuantity = old_inventory_quantity,
        option1 = option1,
        option2 = option2,
        option3 = option3,
        position = position,
        price = price,
        productId = product_id,
        sku = sku,
        taxable = taxable,
        title = title,
        weight = weight,
        weightUnit = weight_unit
    )

fun List<Variant>.toVariantListModel(): List<VariantModel> =
    (this.map { it.toVariantModel() })

fun ProductsResponse.toProductsModel(): ProductsModel =
    ProductsModel(products.map { it.toProductModel() })