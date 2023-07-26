package com.example.shopify.settings.presenation.address.write

import com.example.shopify.settings.presenation.address.adresses.AllAddressesIntent

sealed interface WriteAddressIntent
{

    data object GetUserId : WriteAddressIntent
    data object ReadAddressFromDatabase : WriteAddressIntent
    data class SaveAddress(val key : String) : WriteAddressIntent


    data class NewSelectedCity(val  city : String) : WriteAddressIntent

    data class NewAddress(val address : String) : WriteAddressIntent

    data class NewLatLong(val latitude : Double,val longitude : Double) : WriteAddressIntent




}