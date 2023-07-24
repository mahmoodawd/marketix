package com.example.shopify.utils.workmanager.discount

import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shopify.R
import com.example.shopify.data.dto.codes.DiscountCode
import com.example.shopify.data.remote.ShopifyRemoteInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DiscountCodesWorker @AssistedInject constructor(
    private val shopifyRemote: ShopifyRemoteInterface,
    private val notificationManager: NotificationManager,
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {

        val discountResponse = shopifyRemote.getDiscountCodes()

        discountResponse?.let {
            val randomCode = discountResponse.discount_codes.shuffled().first()
            notificationBuilder(context = context , randomCode)
        }

        return Result.success()
    }


    private fun notificationBuilder(context: Context,discountCode : DiscountCode) {
        Log.d("discountCode",discountCode.toString())
        val notification = NotificationCompat
            .Builder(context, context.getString(R.string.notificationBuilder))
            .setSmallIcon(R.drawable.discount)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.account))
            .setContentTitle(context.getString(R.string.discount_code))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentText(context.getString(R.string.free_discount_code)+discountCode.code).build()

        notificationManager.notify(1000, notification)
    }
}