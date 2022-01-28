package com.breaktime.lab1.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.lang.Exception
import kotlin.random.Random


class GuitarHero @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private var dotSize: Float = 0f
    private var dotPadding: Float = 0f
    private var amount = 4
    private var colours = listOf(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW)
    private var dotsOnScreen = 3
    private var dots: MutableList<Coordinate> = mutableListOf()
    private lateinit var basisList: List<Float>
    private val paint = Paint()
    private var dotClickListener: (() -> Unit)? = null

    private var between: Float = 0f

    private val updateDots = Thread {
        val initDots = List(dotsOnScreen) { Random.nextInt(0, dotsOnScreen + 1) }
        var y = 0f
        initDots.forEach { line ->
            dots.add(Coordinate(basisList[line], y, colours[line]))
            y += between
        }

        while (true) {
            dots.forEach {
                it.second++
                if (it.second == between * 3) {
                    val line = Random.nextInt(0, dotsOnScreen + 1)
                    it.first = basisList[line]
                    it.second = 0f
                    it.color = colours[line]
                }
                invalidate()
            }
            try {
                Thread.sleep(3)
            } catch (e: Exception) {
                Log.e("ERROR", "interrupt")
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val point = width / (amount * 3 + 1f)
        dotPadding = point
        dotSize = point * 2
        val nextCord: () -> Float = { dotPadding + dotSize }
        val cordsGenerator =
            generateSequence(dotPadding + dotSize / 2) { it + nextCord() }
        basisList = cordsGenerator.take(amount).toList()

        between = (height - 2 * dotSize) / 3f

        updateDots.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        updateDots.interrupt()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN)
            basisList.forEach { lineX ->
                if (event.x > lineX && event.x < lineX + dotSize
                    && event.y > height - 3 * dotSize / 2 && event.y < height - dotSize / 2
                ) {
                    dots.forEach dots@{ (x, y, _) ->
                        if (x == lineX && y > height - 3 * dotSize) {
                            dotClickListener?.invoke()
                            return@dots
                        }
                    }
                }
            }

        return super.dispatchTouchEvent(event)
    }

    fun setOnDotClickListener(listener: () -> Unit) {
        dotClickListener = listener
    }

    override fun onDraw(canvas: Canvas?) {
        paint.style = Paint.Style.FILL

        dots.forEach { (x, y, color) ->
            paint.color = color
            canvas?.drawCircle(x, dotSize + y, dotSize / 2, paint)
        }

        paint.style = Paint.Style.STROKE
        basisList.zip(colours).forEach {
            paint.color = Color.GRAY
            paint.strokeWidth = 1f
            canvas?.drawLine(
                it.first,
                dotSize,
                it.first,
                height - dotSize,
                paint
            )

            paint.strokeWidth = 5f
            paint.color = it.second
            canvas?.drawCircle(
                it.first,
                height - dotSize,
                dotSize / 2,
                paint
            )
        }

    }

    data class Coordinate(var first: Float, var second: Float, var color: Int)
}