package com.pixora.volumemax

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

/** Altavoz visual Thunder Deck. Su intensidad representa el nivel seleccionado, no audio capturado. */
class ThunderSpeakerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    var intensity: Int = 0
        set(value) {
            field = value.coerceIn(0, 200)
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val radius = min(width, height) * .39f
        val topY = height * .28f
        val bottomY = height * .70f
        val heat = intensity / 200f
        val glow = Color.HSVToColor(floatArrayOf(195f * (1f - heat), .85f, 1f))

        paint.shader = RadialGradient(cx, topY, radius, intArrayOf(Color.rgb(70, 75, 90), Color.rgb(18, 20, 29), Color.BLACK), null, Shader.TileMode.CLAMP)
        canvas.drawCircle(cx, topY, radius * .52f, paint)
        paint.shader = RadialGradient(cx, bottomY, radius, intArrayOf(Color.rgb(63, 67, 81), Color.rgb(14, 16, 24), Color.BLACK), null, Shader.TileMode.CLAMP)
        canvas.drawCircle(cx, bottomY, radius, paint)
        paint.shader = null

        linePaint.color = Color.argb(190, Color.red(glow), Color.green(glow), Color.blue(glow))
        linePaint.setShadowLayer(12f, 0f, 0f, glow)
        canvas.drawCircle(cx, bottomY, radius * .91f, linePaint)
        linePaint.clearShadowLayer()
        linePaint.color = Color.rgb(88, 91, 105)
        repeat(4) { canvas.drawCircle(cx, bottomY, radius * (.22f + it * .17f), linePaint) }
    }
}
