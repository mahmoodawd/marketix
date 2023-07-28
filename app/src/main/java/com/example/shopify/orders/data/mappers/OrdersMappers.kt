package com.example.shopify.orders.data.mappers

import com.example.shopify.orders.data.dto.LineItem
import com.example.shopify.orders.data.dto.Order
import com.example.shopify.orders.data.dto.OrdersResponse
import com.example.shopify.orders.domain.model.LineItemModel
import com.example.shopify.orders.domain.model.OrderModel
import com.example.shopify.orders.domain.model.OrdersModel

fun OrdersResponse.toOrdersModel(): OrdersModel =
    OrdersModel(orders = orders.map { it.toOrderModel() })

fun Order.toOrderModel(): OrderModel =
    OrderModel(
        contactEmail = contact_email,
        createdAt = created_at,
        currentTotalPrice = current_total_price,
        email = email,
        id = id,
        lineItems = line_items.toLineItemsModel(),
        name = name,
        number = number,
        orderNumber = order_number,
        subtotalPrice = subtotal_price,
        tags = tags,
        test = test,
        totalLineItemsPrice = total_line_items_price,
        totalPrice = total_price,
    )

fun LineItem.toLineItemModel(): LineItemModel =
    LineItemModel(

        id = id,
        name = name,
        price = price,
        productId = product_id,
        properties = properties ?: listOf(),
        quantity = quantity,
        title = title,
        variantId = variant_id,
        variantTitle = variant_title,
        vendor = vendor
    )

fun List<LineItem>.toLineItemsModel(): List<LineItemModel> = (this.map { it.toLineItemModel() })


