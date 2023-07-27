package com.example.shopify.settings.presenation.address.adresses

import java.text.FieldPosition

sealed interface AllAddressesIntent{

    data object GetUserId : AllAddressesIntent

    data class ItemIsDragged(val draggedPosition : Int , val targetPosition: Int) : AllAddressesIntent

    data object GetAllAddresses :  AllAddressesIntent

    data class DeleteAddress(val position : Int) : AllAddressesIntent


    data class LatLongFromGPS(val latitude : Double,val longitude : Double) : AllAddressesIntent

}