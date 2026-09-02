package com.pixora.volumemax

import android.media.audiofx.LoudnessEnhancer

/** Aplica ganancia únicamente a la sesión de audio del reproductor de esta app. */
class SessionGainController {
    private var enhancer: LoudnessEnhancer? = null

    fun attach(audioSessionId: Int): Boolean {
        release()
        return runCatching {
            enhancer = LoudnessEnhancer(audioSessionId).apply { enabled = true }
        }.isSuccess
    }

    fun setGainDb(db: Int): Boolean = runCatching {
        val safeDb = db.coerceIn(0, MAX_GAIN_DB)
        enhancer?.apply {
            setTargetGain(safeDb * 100)
            enabled = safeDb > 0
        } ?: error("No hay una sesión de audio compatible")
    }.isSuccess

    fun release() {
        enhancer?.release()
        enhancer = null
    }

    companion object {
        const val MAX_GAIN_DB = 6
    }
}
