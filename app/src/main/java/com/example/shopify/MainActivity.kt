package com.example.shopify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
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
import com.example.shopify.utils.ui.goneIf
import com.example.shopify.utils.ui.visibleIf
import com.example.shopify.utils.workmanager.discount.DiscountCodesWorker
import com.example.shopify.utils.workmanager.exchnage.ApiExchangeWorker
import com.google.common.util.concurrent.Service.State
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
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


    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        changeStatusBarColor()
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
        currentFragmentObserver()
        updateCurrencyValuePeriodicallyWorkRequest()
        currentFragmentObserver()
        currencyChangeObserver()
        connectivityObserver()
        showNotificationPeriodicTimeRequest()

    }

    private fun changeStatusBarColor()
    {
            window.statusBarColor= ContextCompat.getColor(this, R.color.seed)
    }


    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            getString(R.string.notificationBuilder),
            getString(R.string.notificationBuilder),
            importance
        )
        channel.description = getString(R.string.notificationBuilder)
        notificationManager.createNotificationChannel(channel)
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
                ).build()
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

    private fun showNotificationPeriodicTimeRequest() {
        val workRequest =
            PeriodicWorkRequestBuilder<DiscountCodesWorker>(1, TimeUnit.HOURS)
                .setInitialDelay(0, TimeUnit.MINUTES)
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.LINEAR,
                    duration = Duration.ofMinutes(5)
                ).build()
        workManager.enqueue(workRequest)

    }

    private fun connectivityObserver() {
        lifecycleScope.launch {
            connectivityObserver.observe().collectLatest { state ->
                delay(200)
               binding.internetIsLost.root visibleIf   (state == ConnectivityObserver.Status.Lost || state ==  ConnectivityObserver.Status.Unavailable)
            }
        }
    }


    private fun currentFragmentObserver() {

        lifecycleScope.launch {

            navController.currentBackStackEntryFlow.collectLatest {

                when (it.destination.id) {
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

    override fun onDestroy() {
        super.onDestroy()
        workManager.cancelAllWork()
    }


}
