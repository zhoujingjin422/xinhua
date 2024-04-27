package com.xinhua.language.wanbang.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PaintCanvasView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var paint = Paint()
    private var path = Path()
    private var currentX = 0f
    private var currentY = 0f

    init {
        paint.apply {
            color = Color.BLACK
            isAntiAlias = true
            strokeWidth = 20f
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                currentX = x
                currentY = y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = Math.abs(x - currentX)
                val dy = Math.abs(y - currentY)
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    path.quadTo(currentX, currentY, (x + currentX) / 2, (y + currentY) / 2)
                    currentX = x
                    currentY = y
                }
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                path.lineTo(currentX, currentY)
                invalidate()
                return true
            }
        }
        return false
    }

    companion object {
        private const val TOUCH_TOLERANCE = 4f
    }
    fun clearCanvas() {
        path.reset()
        invalidate()
    }
}
