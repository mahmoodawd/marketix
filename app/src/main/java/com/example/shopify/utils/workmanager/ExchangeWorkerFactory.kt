package com.example.shopify.utils.workmanager

import android.app.NotificationManager
import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.shopify.data.datastore.DataStoreUserPreferences
import com.example.shopify.data.remote.ExchangeApi
import com.example.shopify.data.remote.ShopifyRemoteInterface
import com.example.shopify.utils.workmanager.discount.DiscountCodesWorker
import com.example.shopify.utils.workmanager.exchnage.ApiExchangeWorker
import javax.inject.Inject


class ExchangeWorkerFactory @Inject constructor(private val exchangeApi: ExchangeApi
,private val datastore : DataStoreUserPreferences
,private val shopifyRemoteInterface: ShopifyRemoteInterface
,private val notificationManager : NotificationManager
) : WorkerFactory() {
    override fun createWorker(
        context: Context,
        workerClassName: String,
        workerParams: WorkerParameters
    ): ListenableWorker{

      return  when(workerClassName)
        {
            ApiExchangeWorker::class.java.name ->{
                ApiExchangeWorker(exchangeApi = exchangeApi, dataStore = datastore, context = context,workerParams = workerParams)
            }


          DiscountCodesWorker::class.java.name ->
          {
              DiscountCodesWorker(shopifyRemoteInterface,notificationManager,context,workerParams)
          }


          else -> { ApiExchangeWorker(exchangeApi = exchangeApi, dataStore = datastore, context = context,workerParams = workerParams)}
      }

    }
}