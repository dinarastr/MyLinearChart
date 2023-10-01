package ru.dinarastepina.myapplication.presentation.utils

import com.scichart.charting.model.dataSeries.XyDataSeries

inline fun <reified TX : Comparable<TX>, reified TY : Comparable<TY>> XyDataSeries(seriesName: String? = null): XyDataSeries<TX, TY> {
    return XyDataSeries(
        TX::class.javaObjectType,
        TY::class.javaObjectType
    ).apply { this.seriesName = seriesName }
}