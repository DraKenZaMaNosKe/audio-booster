package com.pixora.volumemax

import android.app.Notification
import android.content.Intent
import android.media.session.MediaController
import android.media.session.MediaSession
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

/** Refleja metadatos públicos de la notificación multimedia; no captura ni copia audio. */
class MediaObserverService : NotificationListenerService() {
    private var controller: MediaController? = null
    private val callback = object : MediaController.Callback() {
        override fun onMetadataChanged(metadata: android.media.MediaMetadata?) = publish()
        override fun onPlaybackStateChanged(state: android.media.session.PlaybackState?) = publish()
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        activeNotifications?.firstOrNull(::hasMediaSession)?.let(::observe)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (hasMediaSession(sbn)) observe(sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        if (sbn.packageName == controller?.packageName) publishUnavailable()
    }

    override fun onDestroy() {
        controller?.unregisterCallback(callback)
        super.onDestroy()
    }

    private fun hasMediaSession(sbn: StatusBarNotification): Boolean = mediaToken(sbn) != null

    private fun observe(sbn: StatusBarNotification) {
        val token = mediaToken(sbn) ?: return
        controller?.unregisterCallback(callback)
        controller = MediaController(this, token).also { it.registerCallback(callback) }
        publish()
    }

    private fun publish() {
        val metadata = controller?.metadata
        val title = metadata?.description?.title?.toString().orEmpty()
        val artist = metadata?.description?.subtitle?.toString().orEmpty()
        latestSnapshot = MediaSnapshot(title, artist, controller?.packageName.orEmpty())
        sendBroadcast(
            Intent(ACTION_MEDIA_UPDATE).setPackage(packageName)
                .putExtra(EXTRA_TITLE, title)
                .putExtra(EXTRA_ARTIST, artist)
                .putExtra(EXTRA_SOURCE, controller?.packageName.orEmpty())
        )
    }

    private fun publishUnavailable() {
        latestSnapshot = MediaSnapshot()
        sendBroadcast(Intent(ACTION_MEDIA_UPDATE).setPackage(packageName))
    }

    @Suppress("DEPRECATION")
    private fun mediaToken(sbn: StatusBarNotification): MediaSession.Token? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            sbn.notification.extras.getParcelable(
                Notification.EXTRA_MEDIA_SESSION,
                MediaSession.Token::class.java
            )
        } else {
            sbn.notification.extras.getParcelable(Notification.EXTRA_MEDIA_SESSION)
        }

    companion object {
        data class MediaSnapshot(
            val title: String = "",
            val artist: String = "",
            val source: String = ""
        )

        @Volatile private var latestSnapshot = MediaSnapshot()

        fun currentSnapshot(): MediaSnapshot = latestSnapshot

        const val ACTION_MEDIA_UPDATE = "com.pixora.volumemax.MEDIA_UPDATE"
        const val EXTRA_TITLE = "title"
        const val EXTRA_ARTIST = "artist"
        const val EXTRA_SOURCE = "source"
    }
}
