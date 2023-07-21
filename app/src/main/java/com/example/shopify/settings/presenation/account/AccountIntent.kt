package com.example.shopify.settings.presenation.account

import android.net.Uri

sealed interface AccountIntent {

    object ConfirmUpdate : AccountIntent


    data class NewImageUri(val uri: Uri) : AccountIntent

    data class NewUserName(val userName: String) : AccountIntent

    data class NewPassword(val password: String) : AccountIntent

    data class NewPhoneNumber(val number: String) : AccountIntent


}