package com.kct.campusshield.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kct.campusshield.R

object NotificationHelper {

    private const val CHANNEL_ID =
        "campusshield_status"

    fun showNotification(
        context: Context,
        title: String,
        message: String
    ) {

        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "CampusShield Status",
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager
                .createNotificationChannel(channel)

        }

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )

    }

}