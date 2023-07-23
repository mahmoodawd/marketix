package com.example.shopify

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shopify.data.datastore.DataStoreUserPreferences
import com.example.shopify.databinding.ActivityMainBinding
import com.example.shopify.utils.connectivity.ConnectivityObserver
import com.example.shopify.utils.workmanager.ApiExchangeWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var dataStore: DataStoreUserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
        currentFragmentObserver()
        updateCurrencyValuePeriodicallyWorkRequest()
        currentFragmentObserver()
        currencyChangeObserver()

    }

    private fun currencyChangeObserver() {
        lifecycleScope.launch(Dispatchers.IO)
        {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                dataStore.getString<String>("currency").distinctUntilChanged().collectLatest {
                 updateCurrencyValueOneTimeWorkRequest()
                }
            }
        }
    }

    private fun updateCurrencyValuePeriodicallyWorkRequest() {
        val workRequest =
            PeriodicWorkRequestBuilder<ApiExchangeWorker>(1, TimeUnit.HOURS)
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.LINEAR,
                    duration = Duration.ofMinutes(5)
                )
                .build()
        workManager.enqueue(workRequest)
    }


    private fun updateCurrencyValueOneTimeWorkRequest() {
        val workRequest =
            OneTimeWorkRequestBuilder<ApiExchangeWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.LINEAR,
                    duration = Duration.ofMinutes(5)
                )
                .build()
        workManager.enqueue(workRequest)
    }


    private fun currentFragmentObserver() {
        navController.addOnDestinationChangedListener { _: NavController?, _: NavDestination?, _: Bundle? ->
            when (navController.currentDestination!!.id) {
                R.id.homeFragment, R.id.favoritesFragment, R.id.settingsFragment, R.id.cartFragment -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomNavigation.visibility = View.GONE
                }

            }

        }

    }


}
