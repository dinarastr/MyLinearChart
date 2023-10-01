package ru.dinarastepina.myapplication.presentation.utils

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorInt
import com.scichart.drawing.common.SolidPenStyle


fun SolidPenStyle(color: Long, thickness: Float = 1f): SolidPenStyle {
    return SolidPenStyle(color.toInt(), thickness)
}

fun Float.toDip(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
}

fun SolidPenStyle(@ColorInt color: Int, thickness: Float = 1f): SolidPenStyle {
    return SolidPenStyle(color, true, thickness.toDip(), null)
}