package com.example.shopify.favorites.data.mappers

import com.example.shopify.data.dto.DraftOrdersItem
import com.example.shopify.data.dto.LineItem
import com.example.shopify.favorites.domain.model.FavoriteProductModel
import com.example.shopify.favorites.domain.model.FavoritesModel

fun List<DraftOrdersItem>.toFavoritesModel(): FavoritesModel =
    FavoritesModel(products = this.map { item -> item.toFavoriteItem() })

fun DraftOrdersItem.toFavoriteItem(): FavoriteProductModel =
    line_items[0].toProduct().also {
        it.draftOrderId = this@toFavoriteItem.id
        it.currency = this@toFavoriteItem.currency
    }

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