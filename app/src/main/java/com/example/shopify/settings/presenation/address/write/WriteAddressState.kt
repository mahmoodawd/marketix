package com.example.shopify.settings.presenation.address.write

data class WriteAddressState(
    val cities: List<String> = emptyList(),
    val loading: Boolean = true,
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude : Double = 0.0,
    val selectedCity: String = "",
   val newAddress: Boolean = true

)
