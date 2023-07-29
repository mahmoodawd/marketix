package com.example.shopify.utils.workmanager.exchnage

import android.content.Context
import android.content.pm.ServiceInfo
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.shopify.R
import com.example.shopify.data.datastore.DataStoreUserPreferences
import com.example.shopify.data.remote.ExchangeApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.lang.Exception

@HiltWorker
class ApiExchangeWorker @AssistedInject constructor(
    private val exchangeApi: ExchangeApi,
    private val dataStore: DataStoreUserPreferences,
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val notification = NotificationCompat
            .Builder(context, context.getString(R.string.notificationBuilder))
            .setSmallIcon(R.drawable.discount)
            .setContentTitle(context.getString(R.string.discount_code))
            .setContentText(context.getString(R.string.please_wait_to_get_the_exchange_info_s))
            .build()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                1000,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(
                1000,
                notification
            )
        }
    }

    override suspend fun doWork(): Result {
        Log.d("exchangeFactory",  " error")
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