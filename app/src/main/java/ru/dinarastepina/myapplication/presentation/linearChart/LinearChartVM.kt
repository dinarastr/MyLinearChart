package ru.dinarastepina.myapplication.presentation.linearChart

import androidx.lifecycle.ViewModel
import ru.dinarastepina.myapplication.domain.GetChartDataInteractor
import javax.inject.Inject

class LinearChartVM @Inject constructor(
    private val getChartDataInteractor: GetChartDataInteractor
): ViewModel() {

}