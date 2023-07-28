package com.example.shopify.settings.presenation.settings

sealed interface SettingsIntent {


    data object GetCurrencies : SettingsIntent

    data object CheckUserIsGuest : SettingsIntent

    data class SaveNotificationPref(val key : String , val value : Boolean) : SettingsIntent
    
    data class SaveLocationPref(val key : String , val value : Boolean) : SettingsIntent

    data class SaveCurrencyPref(val key : String , val value : String) : SettingsIntent

}