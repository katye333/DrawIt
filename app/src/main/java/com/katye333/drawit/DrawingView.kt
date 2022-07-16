package com.katye333.drawit

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

// Used as a View -- Not Activity
class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mDrawPath : CustomPath? = null
    private var mCanvasBitmap : Bitmap? = null
    private var mDrawPaint : Paint? = null
    private var mCanvasPaint : Paint? = null
    private var canvas : Canvas? = null
    private var mBrushSize : Float = 0.toFloat()
    private var color = Color.BLACK

    init {
        setupDrawing()
    }

    private fun setupDrawing() {
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)

        // non-null assertion works because we previously set this variable
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND

        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mBrushSize = 20.toFloat()
    }

    internal inner class CustomPath(var color: Int,
                                    var brushThickness: Float) : Path() {

    }
}