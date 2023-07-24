package com.example.shopify.orders.data.mappers

import com.example.shopify.orders.data.dto.Customer
import com.example.shopify.orders.data.dto.LineItem
import com.example.shopify.orders.data.dto.Order
import com.example.shopify.orders.data.dto.OrdersResponse
import com.example.shopify.orders.domain.model.CustomerModel
import com.example.shopify.orders.domain.model.LineItemModel
import com.example.shopify.orders.domain.model.OrderModel
import com.example.shopify.orders.domain.model.OrdersModel

fun OrdersResponse.toOrdersModel(): OrdersModel =
    OrdersModel(orders = orders.map { it.toOrderModel() })

fun Order.toOrderModel(): OrderModel =
    OrderModel(
        contactEmail = contact_email,
        createdAt = created_at,
        currentSubtotalPrice = current_subtotal_price,
        currentTotalDiscounts = current_total_discounts,
        currentTotalPrice = current_total_price,
        customer = customer.toCustomerModel(),
        email = email,
        id = id,
        lineItems = line_items.toLineItemsModel(),
        name = name,
        number = number,
        orderNumber = order_number,
        orderStatusUrl = order_status_url,
        subtotalPrice = subtotal_price,
        tags = tags,
        test = test,
        token = token,
        totalDiscounts = total_discounts,
        totalLineItemsPrice = total_line_items_price,
        totalOutstanding = total_outstanding?:"",
        totalPrice = total_price,
        totalWeight = total_weight, updatedAt = updated_at
    )

fun LineItem.toLineItemModel(): LineItemModel =
    LineItemModel(
        fulfillableQuantity = fulfillable_quantity,
        fulfillmentService = fulfillment_service,
        giftCard = gift_card,
        grams = grams,
        id = id,
        name = name,
        price = price,
        productExists = product_exists,
        productId = product_id,
        properties = properties ?: listOf(),
        quantity = quantity,
        sku = sku,
        taxable = taxable,
        title = title,
        totalDiscount = total_discount,
        variantId = variant_id,
        variantInventoryManagement = variant_inventory_management?:"",
        variantTitle = variant_title,
        vendor = vendor
    )

fun List<LineItem>.toLineItemsModel(): List<LineItemModel> = (this.map { it.toLineItemModel() })

fun Customer.toCustomerModel(): CustomerModel =
    CustomerModel(
        createdAt = created_at,
        currency = currency,
        email = email,
        firstName = first_name ?: "",
        id = id,
        lastName = last_name ?: "",
        note = note ?: "",
        phone = phone ?: "",
        tags = tags?:"",
        updatedAt = updated_at,
        verifiedEmail = verified_email
    )
