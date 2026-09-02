package com.pixora.volumemax

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

/**
 * Foreground propio. No copia implementaciones externas: solo mantiene visible el boost
 * mientras Android decide mantener vivo el servicio.
 */
class BoostForegroundService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP_BOOST -> stopBoost()
            else -> startBoostForeground()
        }
        return START_STICKY
    }

    private fun startBoostForeground() {
        createNotificationChannel()
        val stopIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, BoostForegroundService::class.java).setAction(ACTION_STOP_BOOST),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setOngoing(true)
            .addAction(R.drawable.ic_notification, getString(R.string.boost_off), stopIntent)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun stopBoost() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_title),
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val ACTION_START_BOOST = "com.pixora.volumemax.action.START_BOOST"
        const val ACTION_STOP_BOOST = "com.pixora.volumemax.action.STOP_BOOST"
        private const val CHANNEL_ID = "pixora_boost_channel"
        private const val NOTIFICATION_ID = 4201
    }
}
