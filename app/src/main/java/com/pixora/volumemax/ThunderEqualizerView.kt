package com.pixora.volumemax

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import kotlin.math.sin

/** Visual decorativo ligado a la potencia seleccionada; no representa una medición de audio. */
class ThunderEqualizerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    var intensity: Int = 0
        set(value) { field = value.coerceIn(0, 200); invalidate() }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count = 24
        val gap = width * .008f
        val barWidth = (width - gap * (count - 1)) / count
        paint.shader = LinearGradient(0f, 0f, width.toFloat(), 0f,
            intArrayOf(Color.CYAN, Color.rgb(94, 78, 255), Color.MAGENTA, Color.rgb(255, 70, 38)),
            null, Shader.TileMode.CLAMP)
        val energy = .22f + intensity / 200f * .65f
        repeat(count) { i ->
            val wave = .25f + kotlin.math.abs(sin(i * .67f + intensity * .04f)) * .75f
            val h = height * wave * energy
            val left = i * (barWidth + gap)
            canvas.drawRoundRect(left, height - h, left + barWidth, height.toFloat(), barWidth * .3f, barWidth * .3f, paint)
        }
    }
}
