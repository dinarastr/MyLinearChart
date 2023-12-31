package ru.dinarastepina.myapplication.presentation.linearChart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scichart.charting.model.dataSeries.XyDataSeries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.dinarastepina.myapplication.domain.GetChartDataInteractor
import ru.dinarastepina.myapplication.presentation.utils.XyDataSeries
import javax.inject.Inject

class LinearChartVM @Inject constructor(
    private val getChartDataInteractor: GetChartDataInteractor,
) : ViewModel() {


    private val _state: MutableStateFlow<ChartState> = MutableStateFlow(ChartState.Loading)
    val state
        get() = _state.asStateFlow()

    private val _series: XyDataSeries<Double, Double> =
        XyDataSeries<Double, Double>().apply {
            acceptsUnsortedData = true
        }

    private val _xMinMax: MutableStateFlow<Pair<Double, Double>> =
        MutableStateFlow(Pair(0.0, 0.0))
    val xMinMax
        get() = _xMinMax.asStateFlow()

    private val _latestPoint: MutableStateFlow<Point> =
        MutableStateFlow(Point(0.0, 0.0))
    val latestPoint
        get() = _latestPoint.asStateFlow()

    init {
        viewModelScope.launch {
            getChartDataInteractor()
                .onStart {
                    _state.value = ChartState.Loading
                }
                .catch {
                    _state.value = ChartState.Error
                }
                .collect {
                    _series.append(
                        it.second, it.first
                    )
                    _xMinMax.value = _series.yMin to _series.yMax
                    _latestPoint.value = it
                    _state.value = ChartState.Content(
                        dataSeries = _series
                    )
                }
        }
    }
}