package com.example.shopify.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

fun FragmentActivity.snackBarObserver(snackBarFlow: SharedFlow<Int>) {
    lifecycleScope.launch {
        snackBarFlow.shareIn(lifecycleScope, SharingStarted.Eagerly, 0)
            .collectLatest { resourceId ->
                delay(500)
                //     Toast.makeText(this@snackBarObserver, getString(resourceId), Toast.LENGTH_SHORT).show()
                Snackbar.make(
                    this@snackBarObserver.window.decorView.rootView, getString(resourceId),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
    }
}