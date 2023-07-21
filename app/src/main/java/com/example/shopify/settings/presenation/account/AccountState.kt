package com.example.shopify.settings.presenation.account

import android.net.Uri

data class AccountState(
    val userName: String = "",
    val imageFromServerUri: Uri? = null,
    val newImageUri : Uri? = null,
    val password: String = "",
    val phone: String = "",
    val loading: Boolean = true,
    val reAuthenticated : Boolean = false
)
