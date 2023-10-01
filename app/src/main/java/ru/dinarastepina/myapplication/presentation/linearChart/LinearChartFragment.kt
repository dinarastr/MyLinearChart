package ru.dinarastepina.myapplication.presentation.linearChart

import android.animation.TimeInterpolator
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
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
import com.scichart.charting.visuals.axes.AutoRange
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
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.sin

class LinearChartFragment : Fragment() {

    private var _vb: FragmentLinearChartBinding? = null
    private val vb: FragmentLinearChartBinding
        get() = _vb!!

    private val ds1 = XyDataSeries<Double, Double>().apply { seriesName = "Orange Series"; fifoCapacity = FIFO_CAPACITY }
    private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private lateinit var schedule: ScheduledFuture<*>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _vb = FragmentLinearChartBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        SciChartBuilder.init(context);


        vb.chartView.suspendUpdates {
            xAxes { numericAxis  {
                autoRange = AutoRange.Always
                axisTitle = "Time (Seconds)"
                textFormatting = "0.0"
            }}
            yAxes { numericAxis  {
                autoRange = AutoRange.Always
                axisTitle = "Amplitude (Numbers)"
                textFormatting = "0.0"
                cursorTextFormatting = "0.00"
                growBy = DoubleRange(0.1, 0.1)
            }}
            renderableSeries {
                fastLineRenderableSeries { dataSeries = ds1; strokeStyle = SolidPenStyle(0xFFe97064, 2f) }
            }
        }
        schedule = scheduledExecutorService.scheduleWithFixedDelay(insertRunnable, 0, TIME_INTERVAL, TimeUnit.MILLISECONDS)
    }

    var t = 0.0
    private val insertRunnable = Runnable {
        vb.chartView.suspendUpdates {
            val y1 = 3.0 * sin(2 * Math.PI * 1.4 * t * 0.02)
            ds1.append(t, y1)
            t += TIME_INTERVAL / 1000.0
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        schedule.cancel(true)
        _vb = null
    }

    companion object {
        private const val FIFO_CAPACITY = 100
        private const val TIME_INTERVAL: Long = 1000
    }
}

fun SciChartSurface.chartModifiers(init: CollectionContext<IChartModifier>.() -> Unit) {
    CollectionContext<IChartModifier>(chartModifiers, context).apply(init)
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