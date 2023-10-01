package ru.dinarastepina.myapplication.presentation.linearChart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scichart.charting.model.dataSeries.XyDataSeries
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.dinarastepina.myapplication.domain.GetChartDataInteractor
import ru.dinarastepina.myapplication.presentation.utils.XyDataSeries
import javax.inject.Inject

class LinearChartVM @Inject constructor(
    private val getChartDataInteractor: GetChartDataInteractor
): ViewModel() {


    val data: MutableStateFlow<ChartState> = MutableStateFlow(ChartState.Loading)
    private val series: XyDataSeries<Double, Double> = XyDataSeries()

    init {
        viewModelScope.launch {
            getChartDataInteractor()
                .onStart {
                    data.value = ChartState.Loading
                }
                .catch {
                    data.value = ChartState.Error
                }
                .collect {
                    series.append(
                        0.0, 0.0
                    )
                data.value = ChartState.Content(
                    dataSeries = series
                )
            }
        }
    }
}