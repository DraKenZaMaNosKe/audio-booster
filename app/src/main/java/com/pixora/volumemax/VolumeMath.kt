package com.pixora.volumemax

import kotlin.math.roundToInt

/** Conversiones puras entre índices OEM y porcentajes de UI. */
object VolumeMath {
    fun indexToPercent(index: Int, max: Int): Int {
        if (max <= 0) return 0
        return ((index.coerceIn(0, max) * 100f) / max).roundToInt()
    }

    fun percentToIndex(percent: Int, max: Int): Int {
        if (max <= 0) return 0
        return ((max * percent.coerceIn(0, 100)) / 100f).roundToInt()
    }
}
