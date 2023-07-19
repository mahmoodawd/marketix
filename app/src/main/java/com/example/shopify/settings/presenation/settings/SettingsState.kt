package com.example.shopify.settings.presenation.settings

import com.example.shopify.settings.domain.model.CurrencyModel

data class SettingsState(
    val currencies: List<CurrencyModel>? = emptyList(),
    val selectedCurrency : String  = "EGP",
    val notification: Boolean = true,
    val LocationService: Boolean = true,
    val loading : Boolean = true,
    )
