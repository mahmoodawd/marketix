package com.example.shopify.favorites.data.mappers

import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.dto.DraftOrdersItem
import com.example.shopify.data.dto.LineItem
import com.example.shopify.favorites.domain.model.FavoriteProductModel
import com.example.shopify.favorites.domain.model.FavoritesModel

fun DraftOrdersItem.toFavoriteItem(): FavoriteProductModel =
    line_items[0].toProduct().also {
        it.draftOrderId = this.id
    }

fun DraftOrderResponse.toFavoritesModel(): FavoritesModel =
    FavoritesModel(products = draft_orders.map { it.toFavoriteItem() })

fun LineItem.toProduct() =
    FavoriteProductModel(
        draftOrderId = 0L,
        id = product_id,
        title = title,
        price = price,
        vendor = vendor,
        imageSrc = "https://cdn.shopify.com/s/files/1/0790/0712/1687/products/85cc58608bf138a50036bcfe86a3a362.jpg?v=1689452647",

        )