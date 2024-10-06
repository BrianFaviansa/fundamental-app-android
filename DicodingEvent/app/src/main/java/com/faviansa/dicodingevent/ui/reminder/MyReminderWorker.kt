package com.faviansa.dicodingevent.ui.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.faviansa.dicodingevent.R
import com.faviansa.dicodingevent.data.EventRepository
import com.faviansa.dicodingevent.data.local.entity.EventEntity
import com.faviansa.dicodingevent.data.local.room.EventDatabase
import com.faviansa.dicodingevent.data.remote.retrofit.ApiConfig
import com.faviansa.dicodingevent.ui.MainActivity
import com.faviansa.dicodingevent.utils.DateFormat.formatNotificationDateTime

class MyReminderWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    companion object {
        const val WORK_NAME = "MyReminderWorker"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "reminder_channel"
    }

    override suspend fun doWork(): Result {
        try {
            val eventRepository = EventRepository.getInstance(
                ApiConfig.getApiService(),
                EventDatabase.getInstance(applicationContext).eventDao()
            )
            val closestEvents = eventRepository.getClosestEvent()
            if (closestEvents != null) {
                showNotification(closestEvents)
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private fun showNotification(event: EventEntity) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            putExtra("eventId", event.id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val subStringEventName =
            if (event.name.length > 30) "${event.name.substring(0, 30)}..." else event.name
        val formattedDate = formatNotificationDateTime(event.beginTime)
        val notificationText = "$subStringEventName begin at $formattedDate"

        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_dicoding)
                .setContentTitle("Upcoming Event")
                .setContentText(notificationText)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

}