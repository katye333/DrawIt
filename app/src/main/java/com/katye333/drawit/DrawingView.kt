package com.katye333.drawit

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
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
    private val mPaths = ArrayList<CustomPath>()
    private val mUndoPaths = ArrayList<CustomPath>()

    init {
        setupDrawing()
    }

    fun onClickUndo() {
        if (mPaths.size > 0) {
            // Add the item that is being removed from mPaths
            mUndoPaths.add(mPaths.removeAt(mPaths.size - 1))
            invalidate()    // Forces redraw of app
        }
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
        // mBrushSize = 20.toFloat()    // mBrushSize will now be set in the MainActivity
    }

    // Extending the View class
    // Called once the view is inflated
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)      // Uses rgba - 256 possible values
        canvas = Canvas(mCanvasBitmap!!)    // Nullable so not-null assertion must be used
    }

    // Change Canvas back to Canvas? if it fails
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Start drawing at top left corner
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        // Make the draw lines persist on the screen
        // Redraws them every time with the proper color, brushThickness, path
        for (path in mPaths) {
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }

        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color

            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        // Events allowed: Up, Move, Down
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()
                mDrawPath!!.moveTo(touchX!!, touchY!!)
            }
            MotionEvent.ACTION_MOVE -> {
                mDrawPath!!.lineTo(touchX!!, touchY!!)
            }
            MotionEvent.ACTION_UP -> {
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color, mBrushSize)
            }
            else -> return false
        }

        invalidate()
        return true
    }

    // Set the brush size based on the screen size
    fun setSizeForBrush(newSize: Float) {
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                            newSize, resources.displayMetrics)

        mDrawPaint!!.strokeWidth = mBrushSize
    }

    // Overwrite the color value
    fun setColor(newColor: String) {
        color = Color.parseColor(newColor)
        mDrawPaint!!.color = color
    }

    internal inner class CustomPath(var color: Int,
                                    var brushThickness: Float) : Path() {

    }
}