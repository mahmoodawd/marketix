package com.example.shopify.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

 fun Fragment.snackBarObserver(snackBarFlow : SharedFlow<Int>,root : View) {
    lifecycleScope.launch {
        snackBarFlow.collectLatest { resourceId ->
            Snackbar.make(root, getString(resourceId), Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                .setBackgroundTint(
                    ContextCompat.getColor(requireContext(), android.R.color.background_dark)
                )
                .show()
        }
    }
}