package com.pixora.volumemax

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

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
    var onInteractionChanged: ((Boolean) -> Unit)? = null
    private var activePointerId = MotionEvent.INVALID_POINTER_ID
    private var lastTouchAngle = 0f
    private var dragPercent = 0f
    private var interactionStartPercent = 0
    private var isDraggingDial = false
    private val ringMaskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(5, 8, 14)
        style = Paint.Style.STROKE
    }
    private val ledPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private val focusPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(46, 225, 255)
        style = Paint.Style.STROKE
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
        val maskRadius = size * .425f
        ringMaskPaint.strokeWidth = size * .105f
        ringMaskPaint.setShadowLayer(size * .018f, 0f, 0f, Color.BLACK)
        canvas.drawCircle(centerX, centerY, maskRadius, ringMaskPaint)
        if (isDraggingDial) {
            focusPaint.strokeWidth = size * .018f
            focusPaint.setShadowLayer(size * .065f, 0f, 0f, Color.CYAN)
            canvas.drawCircle(centerX, centerY, size * .475f, focusPaint)
        }

        val ledCount = 44
        val normalLevel = percent.coerceAtMost(100) / 100f
        val boost = ((percent - 100).coerceAtLeast(0) / 100f).coerceIn(0f, 1f)
        val litCount = if (percent > 100) ledCount else (normalLevel * ledCount).toInt()
        val pulse = if (boost > 0f) {
            .86f + .14f * sin(System.nanoTime() / 110_000_000.0).toFloat()
        } else 1f
        val innerRadius = size * .385f
        val outerRadius = size * (.455f + boost * .012f)
        ledPaint.strokeWidth = size * (.027f + boost * .012f)

        repeat(ledCount) { index ->
            val progress = index / (ledCount - 1f)
            val angle = Math.toRadians((135f + progress * 270f).toDouble())
            val startX = centerX + cos(angle).toFloat() * innerRadius
            val startY = centerY + sin(angle).toFloat() * innerRadius
            val endX = centerX + cos(angle).toFloat() * outerRadius
            val endY = centerY + sin(angle).toFloat() * outerRadius
            val active = index < litCount
            val baseColor = ledGradientColor(progress)
            val hotColor = blendColor(baseColor, Color.rgb(255, 34, 10), boost * .72f)
            ledPaint.color = if (active) hotColor else Color.argb(48, 22, 80, 108)
            if (active) {
                ledPaint.alpha = ((220 + boost * 35) * pulse).toInt().coerceIn(0, 255)
                ledPaint.setShadowLayer(
                    size * (.024f + boost * .055f) * pulse,
                    0f,
                    0f,
                    hotColor
                )
            } else {
                ledPaint.alpha = 85
                ledPaint.clearShadowLayer()
            }
            canvas.drawLine(startX, startY, endX, endY, ledPaint)
        }

        if (boost > 0f || isDraggingDial) postInvalidateOnAnimation()
    }

    private fun blendColor(from: Int, to: Int, amount: Float): Int {
        val t = amount.coerceIn(0f, 1f)
        return Color.rgb(
            (Color.red(from) + (Color.red(to) - Color.red(from)) * t).toInt(),
            (Color.green(from) + (Color.green(to) - Color.green(from)) * t).toInt(),
            (Color.blue(from) + (Color.blue(to) - Color.blue(from)) * t).toInt()
        )
    }

    private fun ledGradientColor(progress: Float): Int {
        val stops = intArrayOf(
            Color.rgb(20, 105, 255),
            Color.rgb(0, 232, 255),
            Color.rgb(98, 64, 255),
            Color.rgb(244, 30, 255),
            Color.rgb(255, 40, 24)
        )
        val scaled = progress.coerceIn(0f, 1f) * (stops.size - 1)
        val index = scaled.toInt().coerceAtMost(stops.lastIndex - 1)
        return blendColor(stops[index], stops[index + 1], scaled - index)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val radius = hypot(event.x - width / 2f, event.y - height / 2f)
                if (radius > min(width, height) * .49f) return false
                activePointerId = event.getPointerId(0)
                lastTouchAngle = touchAngle(event.x, event.y)
                dragPercent = percent.toFloat()
                interactionStartPercent = percent
                isDraggingDial = true
                parent?.requestDisallowInterceptTouchEvent(true)
                onInteractionChanged?.invoke(true)
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val pointerIndex = event.findPointerIndex(activePointerId)
                if (!isDraggingDial || pointerIndex < 0) return false
                val angle = touchAngle(event.getX(pointerIndex), event.getY(pointerIndex))
                val delta = normalizeAngle(angle - lastTouchAngle)
                dragPercent = (dragPercent + delta / 270f * 200f).coerceIn(0f, 200f)
                percent = dragPercent.roundToInt()
                lastTouchAngle = angle
                onPercentPreview?.invoke(percent)
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (!isDraggingDial) return false
                onPercentChanged?.invoke(percent)
                finishInteraction()
                performClick()
                return true
            }
            MotionEvent.ACTION_POINTER_UP -> {
                if (isDraggingDial && event.getPointerId(event.actionIndex) == activePointerId) {
                    onPercentChanged?.invoke(percent)
                    finishInteraction()
                }
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                if (isDraggingDial) {
                    percent = interactionStartPercent
                    onPercentPreview?.invoke(percent)
                    finishInteraction()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun touchAngle(x: Float, y: Float): Float = Math.toDegrees(
        atan2((y - height / 2f).toDouble(), (x - width / 2f).toDouble())
    ).toFloat()

    private fun normalizeAngle(angle: Float): Float = when {
        angle > 180f -> angle - 360f
        angle < -180f -> angle + 360f
        else -> angle
    }

    private fun finishInteraction() {
        activePointerId = MotionEvent.INVALID_POINTER_ID
        isDraggingDial = false
        parent?.requestDisallowInterceptTouchEvent(false)
        onInteractionChanged?.invoke(false)
        invalidate()
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
