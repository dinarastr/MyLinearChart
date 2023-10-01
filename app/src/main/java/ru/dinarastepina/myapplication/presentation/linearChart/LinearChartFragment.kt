package ru.dinarastepina.myapplication.presentation.linearChart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import javax.inject.Inject
import kotlin.math.sin

class LinearChartFragment : Fragment() {

    private var _vb: FragmentLinearChartBinding? = null
    private val vb: FragmentLinearChartBinding
        get() = _vb!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val component by lazy {
        (requireActivity().application as MyChartApp).component
    }

    private val vm: LinearChartVM by viewModels { viewModelFactory }

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
                fastLineRenderableSeries {
                    dataSeries = ds1; strokeStyle = SolidPenStyle(0xFFe97064, 2f) }
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

