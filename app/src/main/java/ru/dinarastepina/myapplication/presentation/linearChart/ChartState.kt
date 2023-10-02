package ru.dinarastepina.myapplication.presentation.linearChart

import com.scichart.charting.model.dataSeries.XyDataSeries

sealed class ChartState {
    data object Loading : ChartState()
    data object Error : ChartState()
    data class Content(
        val dataSeries: XyDataSeries<Double, Double>
    ) : ChartState()
}

typealias Point = Pair<Double, Double>