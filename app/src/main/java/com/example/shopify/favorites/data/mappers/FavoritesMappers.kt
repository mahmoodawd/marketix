package com.example.shopify.favorites.data.mappers

import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.dto.DraftOrdersItem
import com.example.shopify.data.dto.LineItem
import com.example.shopify.favorites.domain.model.FavoriteProductModel
import com.example.shopify.favorites.domain.model.FavoritesModel
import com.example.shopify.utils.constants.TAG_FAVORITES
import com.google.firebase.auth.FirebaseAuth

fun DraftOrdersItem.toFavoriteItem(): FavoriteProductModel =
    line_items[0].toProduct().also {
        it.draftOrderId = this.id
        it.currency = this.currency
    }

fun DraftOrderResponse.toFavoritesModel(): FavoritesModel =
    FavoritesModel(products = draft_orders.filter { item ->
        item.email == FirebaseAuth.getInstance().currentUser!!.email &&
                item.tags == TAG_FAVORITES
    }.map { it.toFavoriteItem() })

fun LineItem.toProduct() =
    FavoriteProductModel(
        draftOrderId = 0L,
        id = product_id,
        title = title,
        price = price,
        currency = "",
        vendor = vendor,
        imageSrc = properties.takeIf { it.isNotEmpty() }?.first()?.value ?: ""

    )