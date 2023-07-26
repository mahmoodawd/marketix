package com.example.shopify.checkout.presentation.checkout

import com.example.shopify.checkout.data.dto.post.PostOrder
import com.example.shopify.checkout.domain.model.CartItems
import com.example.shopify.home.domain.model.discountcode.DiscountCodeModel
import com.example.shopify.settings.domain.model.AddressModel
import com.example.shopify.settings.presenation.address.adresses.AllAddressesIntent


sealed interface CheckOutIntent
{
    data object GetUserEmail : CheckOutIntent

    data object GetUserPhone : CheckOutIntent

    data object GetAllAddress : CheckOutIntent

    data object GetAllDiscountCodes : CheckOutIntent

    data object GetUserId : CheckOutIntent

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

    data class CreateOrder(val postOrder: PostOrder): CheckOutIntent



}