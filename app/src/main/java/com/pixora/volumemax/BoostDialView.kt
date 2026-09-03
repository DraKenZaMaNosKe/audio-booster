package com.pixora.volumemax

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.SweepGradient
import android.graphics.Matrix
import android.util.AttributeSet
import android.animation.ValueAnimator
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
    private var pulse = 0f

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(100, 113, 101, 170)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(72, 224, 255)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private val knobPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(235, 19, 16, 47)
        style = Paint.Style.FILL
        setShadowLayer(28f, 0f, 8f, Color.argb(180, 126, 74, 255))
    }
    private val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        typeface = android.graphics.Typeface.DEFAULT_BOLD
    }
    private val detailPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { strokeWidth = 3f }

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
        val radius = size * 0.34f
        val stroke = size * 0.055f
        trackPaint.strokeWidth = stroke
        progressPaint.strokeWidth = stroke
        val oval = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        val heat = percent / 200f
        val heatColor = Color.HSVToColor(floatArrayOf(195f * (1f - heat), 0.9f, 1f))
        knobPaint.setShadowLayer(22f + pulse * 18f, 0f, 5f, heatColor)
        progressPaint.shader = SweepGradient(
            centerX,
            centerY,
            intArrayOf(
                Color.rgb(45, 226, 255),
                Color.rgb(65, 120, 255),
                Color.rgb(157, 79, 255),
                Color.rgb(255, 142, 45),
                Color.rgb(255, 48, 68),
                Color.rgb(255, 48, 68)
            ),
            floatArrayOf(0f, .22f, .40f, .59f, .75f, 1f)
        ).apply { setLocalMatrix(Matrix().apply { setRotate(135f, centerX, centerY) }) }

        knobPaint.shader = RadialGradient(centerX - radius * .18f, centerY - radius * .18f, radius,
            intArrayOf(Color.rgb(112, 118, 128), Color.rgb(42, 45, 54), Color.rgb(8, 9, 13)), null, Shader.TileMode.CLAMP)
        canvas.drawCircle(centerX, centerY, radius * 0.78f, knobPaint)
        knobPaint.shader = null
        canvas.drawArc(oval, 135f, 270f, false, trackPaint)
        canvas.drawArc(oval, 135f, 270f * percent / 200f, false, progressPaint)

        detailPaint.color = Color.argb(180, 190, 196, 207)
        repeat(12) { index ->
            val angle = Math.toRadians((index * 30).toDouble())
            val inner = radius * .25f
            val outer = radius * .62f
            canvas.drawLine(
                centerX + kotlin.math.cos(angle).toFloat() * inner,
                centerY + kotlin.math.sin(angle).toFloat() * inner,
                centerX + kotlin.math.cos(angle).toFloat() * outer,
                centerY + kotlin.math.sin(angle).toFloat() * outer,
                detailPaint
            )
        }
        detailPaint.style = Paint.Style.STROKE
        detailPaint.strokeWidth = size * .015f
        detailPaint.color = heatColor
        canvas.drawCircle(centerX, centerY, radius * .66f, detailPaint)
        detailPaint.style = Paint.Style.FILL

        valuePaint.textSize = size * 0.12f
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
        if (event.action == MotionEvent.ACTION_UP) {
            onPercentChanged?.invoke(percent)
            performClick()
        }
        return true
    }

    private fun pulseFeedback() {
        ValueAnimator.ofFloat(0f, 1f, 0f).apply {
            duration = 420
            addUpdateListener {
                pulse = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        pulseFeedback()
        return true
    }
}
