package com.example.shopify.utils.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shopify.data.datastore.DataStoreUserPreferences
import com.example.shopify.data.remote.ExchangeApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ApiExchangeWorker @AssistedInject constructor(
    private val exchangeApi: ExchangeApi,
    private val dataStore: DataStoreUserPreferences,
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val targetCurrency = dataStore.getString<String>("currency").first().data
        targetCurrency?.let {

            try {

                val exchangeResponse = exchangeApi.getExchangeMagicNumber(to = targetCurrency)
                dataStore.putString("currencyFactor",exchangeResponse.info.rate.toString())
                Log.d("exchangeFactory", dataStore.getString<String>("currencyFactor").first().data.toString())
            } catch (e: Exception) {

                Log.d("exchangeFactory", e.message ?: " error")
            }
        } ?: run{
            dataStore.putString("currency","EGP")
        }
        return Result.success()
    }
}