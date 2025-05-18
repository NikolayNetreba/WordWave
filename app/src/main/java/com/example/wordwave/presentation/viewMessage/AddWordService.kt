package com.example.wordwave.presentation.viewMessage

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.wordwave.R

class AddWordService : Service() {

    companion object {
        private const val CHANNEL_ID = "word_wave_channel"
        private const val NOTIFICATION_ID = 101
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "WordWave Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Канал уведомлений для WordWave"
            }
            val manager = Context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, SelectionActivity::class.java).apply {
            Intent.setFlags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("WordWave")
            .setContentText("Нажмите, чтобы сделать скриншот")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)  // Чтобы нельзя было убрать свайпом
            .addAction(
                R.drawable.add_2_24, // иконка кнопки (замени на свою)
                "Сделать скриншот",
                pendingIntent
            )
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}