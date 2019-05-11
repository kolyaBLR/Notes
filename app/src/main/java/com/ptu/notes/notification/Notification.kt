package com.ptu.notes.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.ptu.notes.R
import com.ptu.notes.home.HomeActivity

class Notification(private val context: Context) {

    private val channelId = context.getString(R.string.start_notification)
    private val channelName = "Уведомление"

    fun createNotification(title: String, message: String, isPush: Boolean, noteId: String) {
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(HomeActivity::class.java)
        stackBuilder.addNextIntent(HomeActivity.newIntent(context, isPush, noteId))
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_stat_alarm)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .setSound(alarmSound)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(getChannelNotification())
        }
        notificationManager.notify(0, builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getChannelNotification(): NotificationChannel {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        return NotificationChannel(channelId, channelName, importance)
    }
}