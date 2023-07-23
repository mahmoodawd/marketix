package com.example.shopify

import android.app.Application
import android.content.pm.ConfigurationInfo
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.shopify.utils.workmanager.ExchangeWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ShopifyApplication : Application() , Configuration.Provider{

    @Inject lateinit var workerFactory: ExchangeWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

        override fun getWorkManagerConfiguration() =
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()

}