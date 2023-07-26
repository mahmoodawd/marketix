package com.example.shopify.settings.presenation.address.adresses

sealed interface AllAddressesIntent{

    data object GetUserId : AllAddressesIntent

    data object GetAllAddresses :  AllAddressesIntent

    data class DeleteAddress(val position : Int) : AllAddressesIntent


    data class LatLongFromGPS(val latitude : Double,val longitude : Double) : AllAddressesIntent

}