package com.example.shopify

import android.app.Application
import android.content.pm.ConfigurationInfo
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.shopify.utils.workmanager.ExchangeWorkerFactory
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ShopifyApplication : Application() , Configuration.Provider{

    @Inject lateinit var workerFactory: ExchangeWorkerFactory

    override fun onCreate() {
        super.onCreate()

        val config = CheckoutConfig(
            application = this,
            clientId = "ASDXD8e4laCGFlQoSvqXX1dSMUv4-Mnnb2iShncuhn3WREl7Ktnf8wVPEvLv88gzgRb9bsFkdvq5YZ10",
            environment = Environment.SANDBOX,
            returnUrl = "${BuildConfig.APPLICATION_ID}://paypalpay",
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)


        Timber.plant(Timber.DebugTree())
    }





        override fun getWorkManagerConfiguration() =
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()

}