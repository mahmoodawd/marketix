package com.example.shopify.settings.presenation.address.map

sealed interface AddressIntent {

    object MapLoaded : AddressIntent

    object SaveLastLocation : AddressIntent

    data class NewLatLong(val latitude: Double, val longitude: Double) : AddressIntent




}