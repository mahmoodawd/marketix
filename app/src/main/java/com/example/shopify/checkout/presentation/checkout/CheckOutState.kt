package com.example.shopify.checkout.presentation.checkout

import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.settings.domain.model.AddressModel

data class CheckOutState(
    val email: String = "",
    val phone: String = "",
    val deliveryAddress: AddressModel? = null,
    val discountCode: DiscountCodeModel? = null,
    val totalCost: Double = 0.0,
    val subtotal : Double = 0.0,
    val addresses : List<AddressModel>  = listOf(),
    val discountCodes : List<DiscountCodeModel> = listOf()
    )
