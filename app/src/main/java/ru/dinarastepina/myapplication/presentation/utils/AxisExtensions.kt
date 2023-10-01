package ru.dinarastepina.myapplication.presentation.utils

import android.content.Context
import com.scichart.charting.model.RenderableSeriesCollection
import com.scichart.charting.visuals.SciChartSurface
import com.scichart.charting.visuals.axes.DateAxis
import com.scichart.charting.visuals.axes.IAxis
import com.scichart.charting.visuals.axes.NumericAxis
import com.scichart.charting.visuals.renderableSeries.FastLineRenderableSeries
import com.scichart.core.observable.ObservableCollection

data class CollectionContext<T>(val collection: ObservableCollection<T>, val context: Context)

fun CollectionContext<IAxis>.numericAxis(init: NumericAxis.() -> Unit = {}) = collection.add(
    NumericAxis(context).apply(init))
fun CollectionContext<IAxis>.dateAxis(init: DateAxis.() -> Unit = {}) = collection.add(DateAxis(context).apply(init))

fun RenderableSeriesCollection.fastLineRenderableSeries(init: FastLineRenderableSeries.() -> Unit) { add(
    FastLineRenderableSeries().apply(init)) }
