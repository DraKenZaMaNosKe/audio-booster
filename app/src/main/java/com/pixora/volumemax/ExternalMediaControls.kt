package com.pixora.volumemax

import android.content.ComponentName
import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSessionManager

/** Controles reales de la sesión multimedia externa autorizada por el usuario. */
class ExternalMediaControls(context: Context) {
    private val manager = context.getSystemService(MediaSessionManager::class.java)
    private val listener = ComponentName(context, MediaObserverService::class.java)

    fun previous(): Boolean = withController { it.transportControls.skipToPrevious() }
    fun next(): Boolean = withController { it.transportControls.skipToNext() }
    fun togglePlayPause(): Boolean = withController {
        val playing = it.playbackState?.state == android.media.session.PlaybackState.STATE_PLAYING
        if (playing) it.transportControls.pause() else it.transportControls.play()
    }

    private inline fun withController(action: (MediaController) -> Unit): Boolean = runCatching {
        val controllers = manager.getActiveSessions(listener)
            .filter { it.playbackState != null }
        val controller = controllers.firstOrNull {
            it.playbackState?.state == android.media.session.PlaybackState.STATE_PLAYING
        } ?: controllers.firstOrNull()
            ?: return false
        action(controller)
        true
    }.getOrDefault(false)
}
