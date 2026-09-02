package com.pixora.volumemax

import android.content.Context
import android.media.AudioManager

/**
 * Control limpio del volumen real del stream MUSIC.
 * No falsifica límites de hardware: el slider real va de 0 a getStreamMaxVolume.
 */
class VolumeController(private val context: Context) {

    private val audioManager: AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    val maxVolume: Int
        get() = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

    val currentVolume: Int
        get() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

    val currentPercent: Int
        get() = VolumeMath.indexToPercent(currentVolume, maxVolume)

    fun setVolumeIndex(index: Int, showUi: Boolean = false) {
        val safeIndex = index.coerceIn(0, maxVolume)
        val flags = if (showUi) AudioManager.FLAG_SHOW_UI else 0
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, safeIndex, flags)
    }

    fun setVolumePercent(percent: Int, showUi: Boolean = false) {
        val safePercent = percent.coerceIn(0, 100)
        val targetIndex = VolumeMath.percentToIndex(safePercent, maxVolume)
        setVolumeIndex(targetIndex, showUi)
    }
}
