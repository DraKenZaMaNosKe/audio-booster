package com.pixora.volumemax

import kotlin.math.pow
import kotlin.math.roundToInt

/** Convierte dB a amplitud relativa; es una referencia, no el volumen global de Android. */
object GainMath {
    fun relativePercent(db: Int): Int = relativePercent(db.toFloat())

    fun relativePercent(db: Float): Int =
        (10.0.pow(db.coerceAtLeast(0f) / 20.0) * 100).roundToInt()
}
