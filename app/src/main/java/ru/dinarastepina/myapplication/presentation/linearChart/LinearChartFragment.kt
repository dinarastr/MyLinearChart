package ru.dinarastepina.myapplication.presentation.linearChart

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.scichart.charting.ClipMode
import com.scichart.charting.Direction2D
import com.scichart.charting.model.dataSeries.XyDataSeries
import com.scichart.charting.modifiers.XAxisDragModifier
import com.scichart.charting.modifiers.YAxisDragModifier
import com.scichart.charting.visuals.SciChartSurface
import com.scichart.charting.visuals.annotations.LabelPlacement
import com.scichart.charting.visuals.axes.AutoRange
import com.scichart.charting.visuals.renderableSeries.FastLineRenderableSeries
import com.scichart.data.model.DoubleRange
import com.scichart.extensions.builders.SciChartBuilder
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.dinarastepina.myapplication.MyChartApp
import ru.dinarastepina.myapplication.databinding.FragmentLinearChartBinding
import ru.dinarastepina.myapplication.presentation.ViewModelFactory
import ru.dinarastepina.myapplication.presentation.utils.SolidPenStyle
import ru.dinarastepina.myapplication.presentation.utils.annotationLabel
import ru.dinarastepina.myapplication.presentation.utils.annotationLabels
import ru.dinarastepina.myapplication.presentation.utils.annotations
import ru.dinarastepina.myapplication.presentation.utils.chartModifiers
import ru.dinarastepina.myapplication.presentation.utils.fastLineRenderableSeries
import ru.dinarastepina.myapplication.presentation.utils.horizontalLineAnnotation
import ru.dinarastepina.myapplication.presentation.utils.numericAxis
import ru.dinarastepina.myapplication.presentation.utils.pinchZoomModifier
import ru.dinarastepina.myapplication.presentation.utils.renderableSeries
import ru.dinarastepina.myapplication.presentation.utils.suspendUpdates
import ru.dinarastepina.myapplication.presentation.utils.sweepAnimation
import ru.dinarastepina.myapplication.presentation.utils.xAxes
import ru.dinarastepina.myapplication.presentation.utils.xAxisDragModifier
import ru.dinarastepina.myapplication.presentation.utils.yAxes
import ru.dinarastepina.myapplication.presentation.utils.yAxisDragModifier
import ru.dinarastepina.myapplication.presentation.utils.zoomExtentsModifier
import ru.dinarastepina.myapplication.presentation.utils.zoomPanModifier
import javax.inject.Inject

class LinearChartFragment : Fragment() {

    private var _vb: FragmentLinearChartBinding? = null
    private val vb: FragmentLinearChartBinding
        get() = _vb!!

    private val vm: LinearChartVM by viewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy {
        (requireActivity().application as MyChartApp).component
    }

    private lateinit var xAxisDragModifier: XAxisDragModifier

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _vb = FragmentLinearChartBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        SciChartBuilder.init(context)

        lifecycleScope.launch {
            vm.state.collect { state ->
                when (state) {
                    is ChartState.Loading -> {
                        //show a progress bar
                    }
                    is ChartState.Error -> {
                        //add error annotations so that users could still see already loaded data
                    }
                    is ChartState.Content -> {
                        setUpChart(
                            state.dataSeries)
                    }
                }
            }
        }
    }

    private fun setUpChart(
        xyDataSeries: XyDataSeries<Double, Double>
    ) {
        vb.chartView.suspendUpdates {
            xAxes {
                numericAxis {
                    axisTitle = "Time (Seconds)"
                    textFormatting = "0.0"
                    growBy = DoubleRange(1.0, 1.0)
                }
            }
            yAxes {
                numericAxis {
                    lifecycleScope.launch {
                        vm.xMinMax.collect {
                            visibleRange = DoubleRange(it.first - 10.0, it.second + 10.0)
                        }
                    }
                    axisTitle = "Amplitude (Numbers)"
                    textFormatting = "0.0"
                    cursorTextFormatting = "0.00"
                    growBy = DoubleRange(1.0, 1.0)
                }
            }
            zoomExtentsY()
            renderableSeries {
                    fastLineRenderableSeries {
                        dataSeries = xyDataSeries; strokeStyle =
                        SolidPenStyle(0xFFe97064, 2f)
                        addSweepAnimation()
                    }
            }
            addLatestPointAnnotation(
                vm.latestPoint
            )
            addModifiers()
        }
    }

    private fun FastLineRenderableSeries.addSweepAnimation() {
        sweepAnimation {
            duration = ANIMATION_DELAY
            startDelay = START_DELAY
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    private fun SciChartSurface.addModifiers() {
        chartModifiers {
            xAxisDragModifier { clipModeX = ClipMode.ClipAtMin; xAxisDragModifier = this }
            zoomPanModifier(
                direction2D = Direction2D.XDirection
            ) {
                receiveHandledEvents = true
            }
            pinchZoomModifier(
                direction2D = Direction2D.XDirection
            ) { receiveHandledEvents = true }
        }
    }

    private fun SciChartSurface.addLatestPointAnnotation(latestPoint: StateFlow<Point>) {
        annotations {
            horizontalLineAnnotation {
                lifecycleScope.launch {
                    latestPoint.collect {
                        x1 = it.second
                        y1 = it.first
                    }
                }
                horizontalGravity = Gravity.END
                stroke = SolidPenStyle(
                    0xFF47bde6,
                    2f
                )
                annotationLabels {
                    annotationLabel {
                        labelPlacement = LabelPlacement.Axis
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _vb = null
    }

    companion object {
        private const val ANIMATION_DELAY = 1000L
        private const val START_DELAY = 50L
    }
}

