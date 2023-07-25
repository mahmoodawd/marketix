package com.example.shopify.checkout.presentation.checkout

import com.example.shopify.checkout.domain.model.CartItems
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.settings.domain.model.AddressModel


sealed interface CheckOutIntent
{
    object GetUserEmail : CheckOutIntent

    object GetUserPhone : CheckOutIntent

    object GetAllAddress : CheckOutIntent

    object GetAllDiscountCodes : CheckOutIntent

    data class ChooseAddress(val Address :AddressModel) : CheckOutIntent

    data class ChooseDiscountCode(val discountCode:DiscountCodeModel) : CheckOutIntent

    data class EmitMessage(val message : Int) : CheckOutIntent

    data class UserEditEmail(val email : String) : CheckOutIntent

    data class UserEditPhone(val phone : String) : CheckOutIntent

    data class UserSubTotal(val subtotal : String) : CheckOutIntent

    object PostOrdersFromCart : CheckOutIntent

    object ValidateDiscountCode : CheckOutIntent

    object GetPriceRule : CheckOutIntent

    data class NewCartItems(val cartItems: CartItems) : CheckOutIntent



}