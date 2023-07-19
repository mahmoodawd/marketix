package com.example.shopify.settings.presenation.address.write

sealed interface WriteAddressIntent
{

    data class SaveAddress(val key : String) : WriteAddressIntent


    data class NewSelectedCity(val  city : String) : WriteAddressIntent

    data class NewAddress(val address : String) : WriteAddressIntent


}