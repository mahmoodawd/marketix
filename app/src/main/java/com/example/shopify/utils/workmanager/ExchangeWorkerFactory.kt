package com.example.shopify.utils.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.shopify.data.datastore.DataStoreUserPreferences
import com.example.shopify.data.remote.ExchangeApi
import javax.inject.Inject


class ExchangeWorkerFactory @Inject constructor(private val exchangeApi: ExchangeApi,private val datastore : DataStoreUserPreferences) : WorkerFactory() {
    override fun createWorker(
        context: Context,
        workerClassName: String,
        workerParams: WorkerParameters
    ): ListenableWorker = ApiExchangeWorker(exchangeApi = exchangeApi, dataStore = datastore, context = context,workerParams = workerParams)
}