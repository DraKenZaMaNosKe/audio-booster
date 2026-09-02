package com.pixora.volumemax

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class GlobalBoostService : Service() {
    private val controller = GlobalAudioBoostController()

    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopSelf()
                return START_NOT_STICKY
            }
            ACTION_SET_GAIN -> {
                val gainDb = intent.getFloatExtra(EXTRA_GAIN_DB, 0f)
                startForeground(NOTIFICATION_ID, notification(gainDb))
                if (!controller.start() || !controller.setGainDb(gainDb)) {
                    sendBroadcast(Intent(ACTION_STATE).setPackage(packageName)
                        .putExtra(EXTRA_AVAILABLE, false))
                    stopSelf()
                    return START_NOT_STICKY
                }
                sendBroadcast(Intent(ACTION_STATE).setPackage(packageName)
                    .putExtra(EXTRA_AVAILABLE, true)
                    .putExtra(EXTRA_GAIN_DB, gainDb))
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        controller.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel(CHANNEL_ID, getString(R.string.boost_channel), NotificationManager.IMPORTANCE_LOW)
            )
        }
    }

    private fun notification(gainDb: Float): Notification = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(getString(R.string.global_boost_active))
        .setContentText(getString(R.string.global_boost_notification, GainMath.relativePercent(gainDb)))
        .setOngoing(true)
        .setSilent(true)
        .build()

    companion object {
        const val ACTION_SET_GAIN = "com.pixora.volumemax.SET_GLOBAL_GAIN"
        const val ACTION_STOP = "com.pixora.volumemax.STOP_GLOBAL_GAIN"
        const val ACTION_STATE = "com.pixora.volumemax.GLOBAL_GAIN_STATE"
        const val EXTRA_GAIN_DB = "gain_db"
        const val EXTRA_AVAILABLE = "available"
        private const val CHANNEL_ID = "global_boost"
        private const val NOTIFICATION_ID = 4101
    }
}
