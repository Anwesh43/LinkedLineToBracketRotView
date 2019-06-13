package com.anwesh.uiprojects.linetobracketrotview

/**
 * Created by anweshmishra on 13/06/19.
 */

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color

val nodes : Int = 5
val lines : Int = 2
val strokeFactor : Int = 90
val sizeFactor : Float = 2.9f
val scGap : Float = 0.05f
val scDiv : Double = 0.51
val foreColor : Int = Color.parseColor("#0D47A1")
val backColor : Int = Color.parseColor("#BDBDBD")
val rotDeg : Float = 90f
val lFactor : Float = 4f

fun Int.inverse() : Float = 1f / this
fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.mirrorValue(a : Int, b : Int) : Float {
    val k : Float = scaleFactor()
    return (1 - k) * a.inverse() + k * b.inverse()
}
fun Float.updateValue(dir : Float, a : Int, b : Int) : Float = mirrorValue(a, b) * dir * scGap

fun Canvas.drawLineToBracketRot(i : Int, size : Float, sc1 : Float, sc2 : Float, paint : Paint) {
    val sc1i : Float = sc1.divideScale(i, lines)
    val sc2i : Float = sc2.divideScale(i, lines)
    val sf : Float = 1f - 2 * i
    save()
    translate(size * sc2i * sf, 0f)
    for (j in 0..(lines - 1)) {
        val sci1j : Float = sc1i.divideScale(j, lines)
        save()
        translate(0f, -size * (1f - 2 * j))
        rotate(rotDeg * sci1j)
        drawLine(0f, 0f, 0f, (size / lFactor) * (1f - 2 * j), paint)
        restore()
    }
    restore()
}

fun Canvas.drawLTBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    val size : Float = gap / sizeFactor
    paint.color = foreColor
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.strokeCap = Paint.Cap.ROUND
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    save()
    translate(w / 2, gap * (i + 1))
    drawLineToBracketRot(i, size, sc1, sc2, paint)
    restore()
}
