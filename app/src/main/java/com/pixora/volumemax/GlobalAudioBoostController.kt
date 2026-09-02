package com.pixora.volumemax

import android.media.audiofx.DynamicsProcessing
import android.os.Build
import android.util.Log

/**
 * Modo experimental OEM: inserta DynamicsProcessing en la mezcla global (sesión 0).
 * Android considera obsoleto el uso global de efectos insert; siempre debe existir fallback.
 */
class GlobalAudioBoostController {
    private var processor: DynamicsProcessing? = null

    val isAvailable: Boolean get() = processor != null

    fun start(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return false
        if (processor != null) return true

        return runCatching {
            val effect = DynamicsProcessing(Int.MAX_VALUE, GLOBAL_AUDIO_SESSION, null)
            repeat(effect.channelCount) { channel ->
                val limiter = DynamicsProcessing.Limiter(
                    true,
                    true,
                    0,
                    1f,
                    60f,
                    10f,
                    -0.5f,
                    0f
                )
                effect.setLimiterByChannelIndex(channel, limiter)
            }
            effect.setInputGainAllChannelsTo(0f)
            effect.enabled = true
            processor = effect
            Log.i(TAG, "Global DynamicsProcessing enabled; channels=${effect.channelCount}")
        }.onFailure { Log.e(TAG, "Global DynamicsProcessing unavailable", it) }.isSuccess
    }

    fun setGainDb(gainDb: Float): Boolean = runCatching {
        val effect = processor ?: error("Global processor is not active")
        val safeGain = gainDb.coerceIn(0f, MAX_GAIN_DB)
        effect.setInputGainAllChannelsTo(safeGain)
        Log.i(TAG, "Global input gain applied: ${safeGain}dB")
    }.onFailure { Log.e(TAG, "Could not apply global gain", it) }.isSuccess

    fun release() {
        runCatching { processor?.enabled = false }
        runCatching { processor?.release() }
        processor = null
        Log.i(TAG, "Global DynamicsProcessing released")
    }

    companion object {
        private const val TAG = "AudioBoosterGlobal"
        private const val GLOBAL_AUDIO_SESSION = 0
        const val MAX_GAIN_DB = 6f
    }
}
