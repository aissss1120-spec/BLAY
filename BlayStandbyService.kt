package com.blay.assistant

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder

class BlayStandbyService : Service() {
    companion object {
        private const val CHANNEL = "blay_standby"
        private const val NOTIFICATION_ID = 1801
    }

    override fun onCreate() {
        super.onCreate()
        createChannel()
        val notification = Notification.Builder(this, CHANNEL)
            .setContentTitle("BLAY")
            .setContentText("Standby aktif")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setOngoing(true)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // v1.8 keeps the standby service foundation.
        // A production custom "Blay" wake-word model is not bundled.
        return START_STICKY
    }

    private fun createChannel() {
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL,
                "BLAY Standby",
                NotificationManager.IMPORTANCE_LOW
            )
        )
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
