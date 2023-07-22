package com.example.shopify.checkout.data.mappers
import com.example.shopify.checkout.domain.model.CartItem
import com.example.shopify.checkout.domain.model.CartItems
import com.example.shopify.data.dto.DraftOrderResponse
import com.example.shopify.data.dto.DraftOrdersItem


fun DraftOrderResponse.toCartItems(limits : List<Int>) : CartItems
{
    return CartItems(cartItems = draft_orders.filter { it.tags == "cartItem" }.mapIndexed { index, draftOrdersItem ->
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
        upperLimit = limit
    )
}