package com.pixora.volumemax

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.min

/** Control original de Orbix. Convierte un arco de 270 grados en un valor de 0 a 200 %. */
class BoostDialView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    var percent: Int = 0
        set(value) {
            field = value.coerceIn(0, 200)
            contentDescription = context.getString(R.string.dial_accessibility, field)
            invalidate()
        }

    var onPercentChanged: ((Int) -> Unit)? = null
    var onPercentPreview: ((Int) -> Unit)? = null
    private val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = android.graphics.Typeface.DEFAULT_BOLD
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        isFocusable = true
        isClickable = true
        percent = 0
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = min(width, height).toFloat()
        val centerX = width / 2f
        val centerY = height / 2f
        valuePaint.textSize = size * 0.12f
        valuePaint.setShadowLayer(size * .025f, 0f, size * .01f, Color.BLACK)
        canvas.drawText("$percent%", centerX, centerY + size * 0.04f, valuePaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN && event.action != MotionEvent.ACTION_MOVE &&
            event.action != MotionEvent.ACTION_UP
        ) return super.onTouchEvent(event)

        val rawAngle = Math.toDegrees(
            atan2((event.y - height / 2f).toDouble(), (event.x - width / 2f).toDouble())
        ).toFloat()
        val clockwise = (rawAngle - 135f + 360f) % 360f
        val bounded = if (clockwise > 315f) 0f else clockwise.coerceAtMost(270f)
        percent = (bounded / 270f * 200f).toInt()
        onPercentPreview?.invoke(percent)
        if (event.action == MotionEvent.ACTION_UP) {
            onPercentChanged?.invoke(percent)
            performClick()
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
