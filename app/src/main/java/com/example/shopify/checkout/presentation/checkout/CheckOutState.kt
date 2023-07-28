package com.example.shopify.checkout.presentation.checkout

import com.example.shopify.checkout.domain.model.CartItem
import com.example.shopify.checkout.domain.model.PriceRule
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.settings.domain.model.AddressModel

data class CheckOutState(
    val customerId : String = "",
    val email: String = "",
    val phone: String = "",
    val deliveryAddress: AddressModel? = null,
    val discountCode: DiscountCodeModel? = null,
    val priceRule : PriceRule? = null,
    val totalCost: Double = 0.0,
    val subtotal : Double = 0.0,
    val discountValue :Double =  0.0,
    val currency : String = "EGP",
    val currencyFactor : Double = 1.0,
    val addresses : List<AddressModel>  = listOf(),
    val discountCodes : List<DiscountCodeModel> = listOf(),
    val cartItems: List<CartItem> = emptyList(),
    val loading : Boolean= false,
    val error:String = ""
    )
