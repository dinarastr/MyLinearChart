package ru.dinarastepina.myapplication.data

import kotlinx.coroutines.flow.Flow
import ru.dinarastepina.myapplication.domain.IChartRepository

class ChartRepository: IChartRepository {
    override suspend fun getChartData(): Flow<Int> {

    }
}