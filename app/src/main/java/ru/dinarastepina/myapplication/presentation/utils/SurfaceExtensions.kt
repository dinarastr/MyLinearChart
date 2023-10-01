package ru.dinarastepina.myapplication.presentation.utils

import com.scichart.charting.model.RenderableSeriesCollection
import com.scichart.charting.visuals.SciChartSurface
import com.scichart.charting.visuals.axes.IAxis
import com.scichart.core.framework.ISuspendable
import com.scichart.core.framework.UpdateSuspender

fun SciChartSurface.xAxes(clearCollection: Boolean = false, init: CollectionContext<IAxis>.() -> Unit) {
    if (clearCollection) xAxes.clear()
    CollectionContext<IAxis>(xAxes, context).init()
}
fun SciChartSurface.yAxes(clearCollection: Boolean = false, init: CollectionContext<IAxis>.() -> Unit) {
    if (clearCollection) yAxes.clear()
    CollectionContext<IAxis>(yAxes, context).init()
}

fun SciChartSurface.renderableSeries(init: RenderableSeriesCollection.() -> Unit) {
    renderableSeries.init()
}

inline fun <T : ISuspendable> T.suspendUpdates(crossinline block: T.() -> Unit) {
    UpdateSuspender.using(this) {
        block()
    }
}