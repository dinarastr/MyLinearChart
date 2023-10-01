package ru.dinarastepina.myapplication.presentation.utils

import com.scichart.charting.modifiers.IChartModifier
import com.scichart.charting.visuals.SciChartSurface

fun SciChartSurface.chartModifiers(init: CollectionContext<IChartModifier>.() -> Unit) {
    CollectionContext<IChartModifier>(chartModifiers, context).apply(init)
}