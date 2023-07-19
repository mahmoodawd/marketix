package com.example.shopify.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

fun Fragment.snackBarObserver(snackBarFlow : SharedFlow<Int>) {
    lifecycleScope.launch {
        snackBarFlow.shareIn(lifecycleScope, SharingStarted.Eagerly,0).collectLatest { resourceId ->
            delay(500)
            Toast.makeText(requireContext(), getString(resourceId), Toast.LENGTH_SHORT).show()
        }
    }
}