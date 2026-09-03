package com.pixora.volumemax

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.RectF
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
        val radius = width * .34f
        val topY = height * .18f
        val middleY = height * .46f
        val bottomY = height * .73f
        val heat = intensity / 200f
        val glow = Color.HSVToColor(floatArrayOf(195f * (1f - heat), .85f, 1f))

        paint.shader = null
        paint.color = Color.rgb(15, 17, 22)
        canvas.drawRoundRect(RectF(width * .08f, height * .02f, width * .92f, height * .98f), width * .14f, width * .14f, paint)
        linePaint.color = Color.argb(210, Color.red(glow), Color.green(glow), Color.blue(glow))
        linePaint.strokeWidth = 2.5f
        canvas.drawRoundRect(RectF(width * .10f, height * .025f, width * .90f, height * .975f), width * .12f, width * .12f, linePaint)

        listOf(topY to .48f, middleY to 1f, bottomY to 1f).forEach { (y, scale) ->
            paint.shader = RadialGradient(cx, y, radius, intArrayOf(Color.rgb(82, 87, 98), Color.rgb(20, 22, 28), Color.BLACK), null, Shader.TileMode.CLAMP)
            canvas.drawCircle(cx, y, radius * scale, paint)
            linePaint.color = Color.rgb(92, 98, 112)
            repeat(3) { canvas.drawCircle(cx, y, radius * scale * (.42f + it * .25f), linePaint) }
        }
        paint.shader = null

        linePaint.color = Color.argb(190, Color.red(glow), Color.green(glow), Color.blue(glow))
        linePaint.setShadowLayer(12f, 0f, 0f, glow)
        canvas.drawCircle(cx, bottomY, radius * .91f, linePaint)
        linePaint.clearShadowLayer()
        linePaint.color = Color.rgb(60, 65, 76)
        repeat(5) { index ->
            val y = height * (.88f + index * .014f)
            canvas.drawLine(width * .25f, y, width * .75f, y, linePaint)
        }
    }
}
