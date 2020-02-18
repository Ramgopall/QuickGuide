package com.duke.diskoverusosyal

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.*

class MyNotificationManager(private val context: Context) {

    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification

    fun showNotificationOreo(title: String, message: String, intent: Intent) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            val pendingIntent = PendingIntent.getActivity(context, requestID,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channelId = context.getString(R.string.channel)
            val channelName = "DiskoverULifeStyle"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.canShowBadge()
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(notificationChannel)

            val notification = Notification.Builder(context, channelId)
                    .setSmallIcon(R.drawable.launcher_icon)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                    .setTicker(title)
                    .setWhen(0)
                    .setStyle(Notification.BigTextStyle().bigText(message))
                    .setAutoCancel(false)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.launcher_icon))
                    .setContentText(message)
                    .build()

            notificationManager.notify(Calendar.getInstance().timeInMillis.toInt(), notification)

        }
    }

    fun showNotification(title: String, message: String, intent: Intent) {

        val pendingIntent = PendingIntent.getActivity(context, requestID,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val notificationBuilder = NotificationCompat.Builder(context,R.string.channel.toString())
        notificationBuilder
                .setSmallIcon(R.drawable.launcher_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.launcher_icon))
                .setContentTitle(title)
                .setContentText(message)
                .setLights(Color.GREEN,500,500)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setDefaults(NotificationCompat.DEFAULT_SOUND
                        or NotificationCompat.DEFAULT_VIBRATE
                        or NotificationCompat.FLAG_SHOW_LIGHTS)
                .setLights(Color.GREEN, 4000, 1000).priority = NotificationCompat.PRIORITY_MAX

        notificationBuilder.build().flags = notificationBuilder.build().flags or Notification.FLAG_AUTO_CANCEL
        val notification = notificationBuilder.build()

        notificationManager.notify(Calendar.getInstance().timeInMillis.toInt(), notification)

    }

    companion object {

        private val requestID = 234
    }
}
