package ru.dinarastepina.myapplication.presentation.utils

import com.scichart.charting.Direction2D
import com.scichart.charting.modifiers.ExecuteOn
import com.scichart.charting.modifiers.IChartModifier
import com.scichart.charting.modifiers.PinchZoomModifier
import com.scichart.charting.modifiers.XAxisDragModifier
import com.scichart.charting.modifiers.YAxisDragModifier
import com.scichart.charting.modifiers.ZoomExtentsModifier
import com.scichart.charting.modifiers.ZoomPanModifier
import com.scichart.charting.visuals.SciChartSurface

fun SciChartSurface.chartModifiers(init: CollectionContext<IChartModifier>.() -> Unit) {
    CollectionContext<IChartModifier>(chartModifiers, context).apply(init)
}

fun CollectionContext<IChartModifier>.xAxisDragModifier(init: XAxisDragModifier.() -> Unit = {}) = collection.add(
    XAxisDragModifier().apply(init))
fun CollectionContext<IChartModifier>.yAxisDragModifier(init: YAxisDragModifier.() -> Unit = {}) = collection.add(
    YAxisDragModifier().apply(init))
fun CollectionContext<IChartModifier>.zoomPanModifier(
    direction2D: Direction2D = Direction2D.XyDirection,
    init: ZoomPanModifier.() -> Unit = {}
    ) = collection.add(
    ZoomPanModifier().apply {
        direction = direction2D
        init()
    }
)
fun CollectionContext<IChartModifier>.zoomExtentsModifier(
    direction2D: Direction2D = Direction2D.XyDirection,
    isAnimated: Boolean = false,
    init: ZoomExtentsModifier.() -> Unit = {}
    ) = collection.add(
    ZoomExtentsModifier().apply {
        direction = direction2D
        setIsAnimated(isAnimated)
        init()
    }
)

fun CollectionContext<IChartModifier>.pinchZoomModifier(
    direction2D: Direction2D = Direction2D.XyDirection,
    init: PinchZoomModifier.() -> Unit = {}
) = collection.add(
    PinchZoomModifier().apply {
        direction = direction2D
        init()
    })

