package com.example.shopify.productdetails.data.mappers

import com.example.shopify.productdetails.data.dto.productdetails.ImageX
import com.example.shopify.productdetails.data.dto.productdetails.Option
import com.example.shopify.productdetails.data.dto.productdetails.Product
import com.example.shopify.productdetails.data.dto.productdetails.Variant
import com.example.shopify.productdetails.domain.model.details.ImageModel
import com.example.shopify.productdetails.domain.model.details.OptionModel
import com.example.shopify.productdetails.domain.model.details.ProductsDetailsModel
import com.example.shopify.productdetails.domain.model.details.VariantModel


fun Product.toProductsDetailsModel(): ProductsDetailsModel =
    ProductsDetailsModel(
        handle = this.handle,
        id = this.id,
        image = this.image.toImageModel(),
        images = this.images.toImageListModel(),
        options = this.options.toOptionListModel(),
        productType = this.product_type,
        status = this.status,
        tags = this.tags,
        title = this.title,
        description = this.body_html,
        variants = this.variants?.toVariantListModel(),
        vendor = this.vendor,
        rating = 3.5F
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