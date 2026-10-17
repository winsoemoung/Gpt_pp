package com.example.offlinepos.hardware

import android.graphics.Paint
import android.graphics.Typeface

object ReceiptStyle {
    fun typeface(style: String): Typeface = when (style.uppercase()) {
        "BOLD" -> Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        "ITALIC" -> Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        "BOLD_ITALIC" -> Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
        else -> Typeface.DEFAULT
    }

    fun size(base: Float, size: String): Float = when (size.uppercase()) {
        "SMALL" -> base * 0.82f
        "LARGE" -> base * 1.25f
        else -> base
    }

    fun align(value: String): Paint.Align = when (value.uppercase()) {
        "LEFT" -> Paint.Align.LEFT
        "RIGHT" -> Paint.Align.RIGHT
        else -> Paint.Align.CENTER
    }

    fun apply(paint: Paint, style: String, size: String, align: String, baseSize: Float) {
        paint.typeface = typeface(style)
        paint.textSize = size(baseSize, size)
        paint.textAlign = align(align)
    }
}
