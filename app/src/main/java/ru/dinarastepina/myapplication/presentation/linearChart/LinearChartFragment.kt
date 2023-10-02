package ru.dinarastepina.myapplication.presentation.linearChart

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import com.scichart.charting.visuals.axes.AutoRange
import com.scichart.data.model.DoubleRange
import com.scichart.extensions.builders.SciChartBuilder
import ru.dinarastepina.myapplication.MyChartApp
import ru.dinarastepina.myapplication.databinding.FragmentLinearChartBinding
import ru.dinarastepina.myapplication.presentation.ViewModelFactory
import ru.dinarastepina.myapplication.presentation.utils.SolidPenStyle
import ru.dinarastepina.myapplication.presentation.utils.XyDataSeries
import ru.dinarastepina.myapplication.presentation.utils.fastLineRenderableSeries
import ru.dinarastepina.myapplication.presentation.utils.numericAxis
import ru.dinarastepina.myapplication.presentation.utils.renderableSeries
import ru.dinarastepina.myapplication.presentation.utils.suspendUpdates
import ru.dinarastepina.myapplication.presentation.utils.xAxes
import ru.dinarastepina.myapplication.presentation.utils.yAxes
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import com.scichart.charting.ClipMode
import com.scichart.charting.model.dataSeries.XyDataSeries
import com.scichart.charting.modifiers.XAxisDragModifier
import com.scichart.charting.modifiers.YAxisDragModifier
import com.scichart.charting.visuals.annotations.AnnotationCoordinateMode
import com.scichart.charting.visuals.annotations.AnnotationLabel
import com.scichart.charting.visuals.annotations.HorizontalLineAnnotation
import com.scichart.charting.visuals.annotations.LabelPlacement
import kotlinx.coroutines.launch
import ru.dinarastepina.myapplication.presentation.utils.annotationLabel
import ru.dinarastepina.myapplication.presentation.utils.annotationLabels
import ru.dinarastepina.myapplication.presentation.utils.annotations
import ru.dinarastepina.myapplication.presentation.utils.chartModifiers
import ru.dinarastepina.myapplication.presentation.utils.horizontalLineAnnotation
import ru.dinarastepina.myapplication.presentation.utils.sweepAnimation
import ru.dinarastepina.myapplication.presentation.utils.xAxisDragModifier
import ru.dinarastepina.myapplication.presentation.utils.yAxisDragModifier
import ru.dinarastepina.myapplication.presentation.utils.zoomExtentsModifier
import ru.dinarastepina.myapplication.presentation.utils.zoomPanModifier
import java.util.Collections
import javax.inject.Inject
import kotlin.math.sin

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
    private lateinit var yAxisDragModifier: YAxisDragModifier


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
        SciChartBuilder.init(context);

        when (val state = vm.state.value) {
            is ChartState.Loading -> {
               //show a progress bar
            }
            is ChartState.Error -> {
                //add error annotations so that users could still see already loaded data
            }
            is ChartState.Content -> {
               setUpChart(state.dataSeries)
            }
        }
    }



    private fun setUpChart(xyDataSeries: XyDataSeries<Double, Double>) {
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
                    autoRange = AutoRange.Always
                    axisTitle = "Amplitude (Numbers)"
                    textFormatting = "0.0"
                    cursorTextFormatting = "0.00"
                    growBy = DoubleRange(1.0, 1.0)
                }
            }
            renderableSeries {
                    fastLineRenderableSeries {
                        dataSeries = xyDataSeries; strokeStyle =
                        SolidPenStyle(0xFFe97064, 2f)

                        sweepAnimation {
                            duration = 1000
                            startDelay = 50
                            interpolator = AccelerateDecelerateInterpolator()
                        }
                    }
            }

            chartModifiers {
                xAxisDragModifier { clipModeX = ClipMode.ClipAtMin; xAxisDragModifier = this }
                yAxisDragModifier { yAxisDragModifier = this }
                zoomPanModifier { receiveHandledEvents = true }
                zoomExtentsModifier()
            }
            annotations {
                horizontalLineAnnotation {
                    lifecycleScope.launch {
                        vm.latestPoint.collect {
                            x1 = vm.latestPoint.value.second;
                            y1 = vm.latestPoint.value.first
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _vb = null
    }

    companion object {
        private const val FIFO_CAPACITY = 100
        private const val TIME_INTERVAL: Long = 1000
    }
}

