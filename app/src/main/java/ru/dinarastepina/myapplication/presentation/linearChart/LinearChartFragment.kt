package ru.dinarastepina.myapplication.presentation.linearChart

import android.animation.TimeInterpolator
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import com.scichart.charting.model.RenderableSeriesCollection
import com.scichart.charting.model.dataSeries.XyDataSeries
import com.scichart.charting.modifiers.IChartModifier
import com.scichart.charting.modifiers.ModifierGroup
import com.scichart.charting.modifiers.PinchZoomModifier
import com.scichart.charting.modifiers.ZoomExtentsModifier
import com.scichart.charting.modifiers.ZoomPanModifier
import com.scichart.charting.visuals.SciChartSurface
import com.scichart.charting.visuals.axes.DateAxis
import com.scichart.charting.visuals.axes.IAxis
import com.scichart.charting.visuals.axes.NumericAxis
import com.scichart.charting.visuals.renderableSeries.FastLineRenderableSeries
import com.scichart.charting.visuals.renderableSeries.XyRenderableSeriesBase
import com.scichart.core.framework.ISuspendable
import com.scichart.core.framework.UpdateSuspender
import com.scichart.core.model.DoubleValues
import com.scichart.core.observable.ObservableCollection
import com.scichart.data.model.DoubleRange
import com.scichart.drawing.common.SolidPenStyle
import com.scichart.extensions.builders.AnimatorBuilderBase
import com.scichart.extensions.builders.SciChartBuilder
import ru.dinarastepina.myapplication.databinding.FragmentLinearChartBinding

class LinearChartFragment : Fragment() {

    private var _vb: FragmentLinearChartBinding? = null
    private val vb: FragmentLinearChartBinding
        get() = _vb!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _vb = FragmentLinearChartBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        SciChartBuilder.init(context);


        val series: DoubleSeries = DoubleSeries(5000)
        (1..5000).forEach {
            series.add(it.toDouble(), it.toDouble())
        }

        vb.chartView.suspendUpdates {
            xAxes { numericAxis { visibleRange = DoubleRange(1.0, 5000.0) } }
            yAxes { numericAxis { growBy = DoubleRange(1.0, 100.0) } }
            renderableSeries {
                fastLineRenderableSeries {
                    dataSeries = XyDataSeries<Double, Double>().apply {
                        append(series.xValues, series.yValues)
                    }
                    strokeStyle = SolidPenStyle(0xFFAE418D, 3f)

                    sweepAnimation {
                        duration = Constant.ANIMATION_DURATION
                        startDelay = Constant.ANIMATION_START_DELAY
                        interpolator = DefaultInterpolator.interpolator
                    }
                }
            }
            chartModifiers { defaultModifiers() }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _vb = null
    }
}

class DoubleSeries(capacity: Int) {
    val xValues: DoubleValues
    val yValues: DoubleValues

    init {
        xValues = DoubleValues(capacity)
        yValues = DoubleValues(capacity)
    }

    fun add(x: Double, y: Double) {
        xValues.add(x)
        yValues.add(y)
    }
}
fun SciChartSurface.chartModifiers(init: CollectionContext<IChartModifier>.() -> Unit) {
    CollectionContext<IChartModifier>(chartModifiers, context).apply(init)
}

fun CollectionContext<IChartModifier>.defaultModifiers() {
    val modifierGroup = ModifierGroup().apply {
        childModifiers.add(PinchZoomModifier())
        childModifiers.add(ZoomPanModifier().apply { receiveHandledEvents = true })
        childModifiers.add(ZoomExtentsModifier())
    }
    collection.add(modifierGroup)
}

object DefaultInterpolator {
    val interpolator: Interpolator
        get() = AccelerateDecelerateInterpolator() //new DecelerateInterpolator();
}

class Constant {

    companion object{
        const val ANIMATION_DURATION = 1000L
        const val ANIMATION_START_DELAY = 50L
    }
}

fun <TBuilder : AnimatorBuilderBase.RenderPassDataAnimatorBuilder<TBuilder>> sweepAnimation(builder: TBuilder, init: SweepAnimator.() -> Unit) {
    val animator = SweepAnimator().apply(init)
    animator(builder, animator)
        .withSweepTransformation()
        .start()
}

private fun <TBuilder : AnimatorBuilderBase<TBuilder>> animator(builder: TBuilder, animator: SweepAnimator): TBuilder {
    return builder
        .withInterpolator(animator.interpolator)
        .withDuration(animator.duration)
        .withStartDelay(animator.startDelay)
}

fun <T : XyRenderableSeriesBase> T.sweepAnimation(init: SweepAnimator.() -> Unit) = sweepAnimation(
    SciChartBuilder.instance().newAnimator(this), init)


open class SweepAnimator(var interpolator: TimeInterpolator = LinearInterpolator(), var duration: Long = 3000, var startDelay: Long = 350)


fun SolidPenStyle(color: Long, thickness: Float = 1f): SolidPenStyle {
    return SolidPenStyle(color.toInt(), thickness)
}

fun Float.toDip(): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
}

fun SolidPenStyle(@ColorInt color: Int, thickness: Float = 1f): SolidPenStyle {
    return SolidPenStyle(color, true, thickness.toDip(), null)
}

@Suppress("FunctionName")
inline fun <reified TX : Comparable<TX>, reified TY : Comparable<TY>> XyDataSeries(seriesName: String? = null): XyDataSeries<TX, TY> {
    return XyDataSeries(
        TX::class.javaObjectType,
        TY::class.javaObjectType
    ).apply { this.seriesName = seriesName }

}

fun RenderableSeriesCollection.fastLineRenderableSeries(init: FastLineRenderableSeries.() -> Unit) { add(
    FastLineRenderableSeries().apply(init)) }


fun SciChartSurface.renderableSeries(init: RenderableSeriesCollection.() -> Unit) {
    renderableSeries.init()
}

fun CollectionContext<IAxis>.numericAxis(init: NumericAxis.() -> Unit = {}) = collection.add(
    NumericAxis(context).apply(init))
fun CollectionContext<IAxis>.dateAxis(init: DateAxis.() -> Unit = {}) = collection.add(DateAxis(context).apply(init))


inline fun <T : ISuspendable> T.suspendUpdates(crossinline block: T.() -> Unit) {
    UpdateSuspender.using(this) {
        block()
    }
}

data class CollectionContext<T>(val collection: ObservableCollection<T>, val context: Context)

fun SciChartSurface.xAxes(clearCollection: Boolean = false, init: CollectionContext<IAxis>.() -> Unit) {
    if (clearCollection) xAxes.clear()
    CollectionContext<IAxis>(xAxes, context).init()
}
fun SciChartSurface.yAxes(clearCollection: Boolean = false, init: CollectionContext<IAxis>.() -> Unit) {
    if (clearCollection) yAxes.clear()
    CollectionContext<IAxis>(yAxes, context).init()
}