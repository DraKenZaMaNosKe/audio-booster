package com.pixora.volumemax

import android.content.Context
import android.media.MediaPlayer
import android.media.audiofx.LoudnessEnhancer

/**
 * Sesión propia para reproducción interna. Sirve para aplicar LoudnessEnhancer
 * en una canción reproducida desde esta app, no para interceptar audio de terceros.
 */
class LocalAudioBoost(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var loudnessEnhancer: LoudnessEnhancer? = null

    fun attachToCurrentSession(sessionId: Int) {
        releaseEnhancerOnly()
        loudnessEnhancer = LoudnessEnhancer(sessionId).apply {
            enabled = true
        }
    }

    fun attachToPlayer(player: MediaPlayer) {
        mediaPlayer = player
        attachToCurrentSession(player.audioSessionId)
    }

    fun setBoostDb(db: Int) {
        val safeDb = db.coerceIn(0, 12)
        loudnessEnhancer?.setTargetGain(safeDb * 100)
    }

    fun setEnabled(enabled: Boolean) {
        loudnessEnhancer?.enabled = enabled
    }

    fun releaseEnhancerOnly() {
        loudnessEnhancer?.release()
        loudnessEnhancer = null
    }

    fun releaseAll() {
        releaseEnhancerOnly()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
