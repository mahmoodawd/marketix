package com.example.shopify.auth.presentation.googleauth

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.shopify.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse

class AuthResultContract : ActivityResultContract<Int, IdpResponse>() {

    companion object {
        const val INPUT_INT = "input_int"
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    override fun createIntent(context: Context, input: Int): Intent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setLogo(R.drawable.ic_launcher_foreground)
        .setTheme(R.style.Theme_Shopify)
        .setAvailableProviders(providers)
        .setIsSmartLockEnabled(true)
        .build().apply { putExtra(INPUT_INT, input) }


    override fun parseResult(resultCode: Int, intent: Intent?): IdpResponse =
        IdpResponse.fromResultIntent(intent)!!


}