package ru.dinarastepina.myapplication.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.dinarastepina.myapplication.domain.IChartRepository
import javax.inject.Inject

class ChartRepository @Inject constructor(): IChartRepository {
    override suspend fun getChartData(): Flow<Double> {
        return flow {
            (0..100).forEach {
                emit(it.toDouble())
                delay(1000)
            }
        }
    }
}