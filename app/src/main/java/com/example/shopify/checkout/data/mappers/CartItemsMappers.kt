package com.example.shopify.checkout.data.mappers
import com.example.shopify.checkout.domain.model.CartItem
import com.example.shopify.checkout.domain.model.CartItems
import com.example.shopify.checkout.domain.model.LineItemModel
import com.example.shopify.checkout.domain.model.PropertyModel
import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.dto.DraftOrdersItem
import com.example.shopify.data.dto.LineItem
import com.example.shopify.data.dto.PropertiesItem
import com.example.shopify.utils.rounder.roundTo


fun DraftOrderResponse.toCartItems(limits : List<Int>,email : String) : CartItems
{
    return CartItems(cartItems = draft_orders.filter { it.email == email && it.tags == "cartItem" }.mapIndexed { index, draftOrdersItem ->
        draftOrdersItem.toCartItem( if (limits.isNotEmpty()) limits[index] else 0)
    })
}
fun DraftOrdersItem.toCartItem(limit : Int): CartItem {
    return CartItem(
        itemId = id,
        itemName = line_items.first().name,
        currency = currency,
        itemPrice = total_price,
        quantity = line_items.first().quantity.toString(),
        imageUrl = line_items.first().properties.first().value,
        upperLimit = limit,
        oneItemPrice = (total_price.toDouble()/line_items.first().quantity).roundTo(2).toString(),
        variantId = line_items.first().variant_id.toString(),

    )
}

//fun LineItem.toLineItemModel() : LineItemModel
//{
//    return LineItemModel(quantity = quantity, variantId =  variant_id , properties = properties.map { it.toPropertyModel() })
//}
//
//fun PropertiesItem.toPropertyModel() : PropertyModel
//{
//    return PropertyModel(imageUrl,value)
//}