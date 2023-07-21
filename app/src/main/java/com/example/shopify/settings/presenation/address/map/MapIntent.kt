package com.example.shopify.settings.presenation.address.map

sealed interface MapIntent {

    object MapLoaded : MapIntent

    data class NewLatLong(val latitude: Double, val longitude: Double) : MapIntent




}