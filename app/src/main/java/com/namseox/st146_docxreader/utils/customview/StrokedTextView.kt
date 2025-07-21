package com.namseox.st146_docxreader.utils.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView

class StrokedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var strokeGradient: Shader? = null
    private var strokeWidth: Float = 6f
    private var strokeColor: Int = Color.WHITE

    private val strokePaint = TextPaint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val fillPaint = TextPaint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private var gradient: Shader? = null

    override fun onDraw(canvas: Canvas) {
        val text = text ?: return

        strokePaint.textSize = textSize
        strokePaint.typeface = typeface
        strokePaint.strokeWidth = strokeWidth
        strokePaint.color = strokeColor
        strokePaint.shader = strokeGradient


        fillPaint.textSize = textSize
        fillPaint.typeface = typeface
        fillPaint.color = currentTextColor
        fillPaint.shader = gradient

        val availableWidth = width - paddingLeft - paddingRight

        val layout = StaticLayout.Builder
            .obtain(text, 0, text.length, strokePaint, availableWidth)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
            .setIncludePad(true)
            .build()

        val totalHeight = layout.height
        val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK
        val offsetY = when (verticalGravity) {
            Gravity.CENTER_VERTICAL -> (height - totalHeight) / 2f
            Gravity.BOTTOM -> (height - totalHeight).toFloat() - paddingBottom
            else -> paddingTop.toFloat() // default is top
        }

        // Xác định căn lề ngang
        val horizontalGravity = gravity and Gravity.HORIZONTAL_GRAVITY_MASK
        val paintAlign = when (horizontalGravity) {
            Gravity.CENTER_HORIZONTAL -> Paint.Align.CENTER
            Gravity.RIGHT -> Paint.Align.RIGHT
            else -> Paint.Align.LEFT
        }
        strokePaint.textAlign = paintAlign
        fillPaint.textAlign = paintAlign

        canvas.save()
        canvas.translate(0f, offsetY)

        layout.draw(canvas, strokePaint, paintAlign)
        layout.draw(canvas, fillPaint, paintAlign)

        canvas.restore()
    }

    private fun StaticLayout.draw(canvas: Canvas, paint: TextPaint, align: Paint.Align) {
        val x = when (align) {
            Paint.Align.CENTER -> width / 2f + paddingLeft / 2f - paddingRight / 2f
            Paint.Align.RIGHT -> width - paddingRight.toFloat()
            else -> paddingLeft.toFloat()
        }

        for (i in 0 until lineCount) {
            val lineStart = getLineStart(i)
            val lineEnd = getLineEnd(i)
            val lineText = text.substring(lineStart, lineEnd)
            canvas.drawText(
                lineText,
                x,
                getLineBaseline(i).toFloat(),
                paint
            )
        }
    }

    fun setStrokeColor(color: Int) {
        strokeColor = color
        invalidate()
    }

    fun setStrokeWidth(width: Float) {
        strokeWidth = width
        invalidate()
    }

    fun setGradientColor(colors: IntArray, positions: FloatArray? = null) {
        post {
            gradient = LinearGradient(
                0f, 0f, width.toFloat(), 0f,
                colors, positions, Shader.TileMode.CLAMP
            )
            invalidate()
        }
    }
    fun setStrokeGradientColor(colors: IntArray, positions: FloatArray? = null) {
        post {
            strokeGradient = LinearGradient(
                0f, 0f, width.toFloat(), 0f,
                colors, positions, Shader.TileMode.CLAMP
            )
            invalidate()
        }
    }
}
