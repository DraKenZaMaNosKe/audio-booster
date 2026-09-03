package com.pixora.volumemax

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

/** Firma digital original de AudioBooster, dibujada como rótulo LED dentro del display. */
class LedBrandView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    private val label = context.getString(R.string.screen_brand_led)
    private val outline = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
    }
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        style = Paint.Style.FILL
    }
    private val scanline = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.argb(70, 37, 224, 255)
        strokeWidth = 1f
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        contentDescription = context.getString(R.string.screen_brand_accessibility)
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val textSize = height * .43f
        val baseline = height * .64f
        outline.textSize = textSize
        outline.strokeWidth = (textSize * .12f).coerceAtLeast(1.2f)
        outline.color = Color.rgb(1, 8, 15)
        outline.setShadowLayer(textSize * .38f, 0f, 0f, Color.rgb(0, 224, 255))

        fill.textSize = textSize
        fill.shader = LinearGradient(
            width * .12f,
            0f,
            width * .88f,
            0f,
            intArrayOf(Color.rgb(49, 235, 255), Color.rgb(139, 92, 255), Color.rgb(255, 76, 211)),
            null,
            Shader.TileMode.CLAMP
        )
        fill.setShadowLayer(textSize * .18f, 0f, 0f, Color.CYAN)

        canvas.drawText(label, width / 2f, baseline, outline)
        canvas.drawText(label, width / 2f, baseline, fill)
        for (y in baseline.toInt() + 2 until height step 3) {
            canvas.drawLine(width * .08f, y.toFloat(), width * .92f, y.toFloat(), scanline)
        }
    }
}
