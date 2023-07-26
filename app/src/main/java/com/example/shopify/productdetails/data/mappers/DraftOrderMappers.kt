package com.example.shopify.productdetails.data.mappers

import com.example.shopify.productdetails.data.dto.draftorder.DraftOrder
import com.example.shopify.productdetails.data.dto.draftorder.DraftOrderRequest
import com.example.shopify.productdetails.data.dto.draftorder.LineItem
import com.example.shopify.productdetails.data.dto.draftorder.PropertyItem
import com.example.shopify.productdetails.domain.model.details.ImageModel
import com.example.shopify.productdetails.domain.model.details.ProductsDetailsModel
import com.example.shopify.utils.constants.TAG_CART
import com.example.shopify.utils.constants.TAG_FAVORITES
import com.google.firebase.auth.FirebaseAuth

fun ProductsDetailsModel.toFavoriteDraftOrderRequest() =
    DraftOrderRequest(
        draft_order = this.toFavoriteDraftOrder()
    )


fun ProductsDetailsModel.toCartDraftOrderRequest(variantId: Long?) =
    DraftOrderRequest(
        draft_order = this.toCartDraftOrder(variantId)
    )


fun ProductsDetailsModel.toFavoriteDraftOrder() =
    DraftOrder(
        email = FirebaseAuth.getInstance().currentUser?.email ?: "",
        tags = TAG_FAVORITES,
        line_items = listOf(this.toLineItem(variants?.first()?.id)),
    )

fun ProductsDetailsModel.toCartDraftOrder(variantId: Long?) =
    DraftOrder(
        email = FirebaseAuth.getInstance().currentUser?.email ?: "",
        tags = TAG_CART,
        line_items = listOf(this.toLineItem(variantId)),
    )


fun ProductsDetailsModel.toLineItem(
    variantId: Long?
) =
    LineItem(
        variant_id = variantId ?: variants?.first()?.id!!,
        quantity = 1,
        properties = listOf(image.toPropertyItem())
    )


fun ImageModel.toPropertyItem() =
    PropertyItem(
        name = "image_url",
        value = src
    )