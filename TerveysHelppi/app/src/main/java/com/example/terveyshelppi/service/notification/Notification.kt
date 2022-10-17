package com.example.terveyshelppi.service.notification

import com.example.terveyshelppi.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.example.terveyshelppi.MainActivity

class Notification : BroadcastReceiver() {
    private val chanelId = "MY_CHANNEL"

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationID", 0)

        //This intent is for coming back to main screen after user click on the notification
        val mainIntent = Intent(context, MainActivity::class.java)

        //pending intent for notification
        val contentIntent =
            PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, chanelId)
        val channelName: CharSequence = "My Notification"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(chanelId, channelName, importance)

        notificationManager.createNotificationChannel(channel)
        builder.setChannelId(chanelId)
        builder.setSmallIcon(R.drawable.noti)
            .setContentTitle("Did you exercise today?")
            .setContentText("Check out new workout program in TerveysHelppi!")
            .setContentIntent(contentIntent)
            .setAutoCancel(false)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .priority = NotificationManager.IMPORTANCE_HIGH
        notificationManager.notify(notificationId, builder.build())
    }
}
