package ru.dinarastepina.myapplication.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.dinarastepina.myapplication.domain.IChartRepository

class ChartRepository: IChartRepository {
    override suspend fun getChartData(): Flow<Int> {
        return flow {
            emit(0)
        }
    }
}