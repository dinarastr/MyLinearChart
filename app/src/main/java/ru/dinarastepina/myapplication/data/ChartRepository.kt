package ru.dinarastepina.myapplication.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.dinarastepina.myapplication.domain.IChartRepository
import javax.inject.Inject
import kotlin.random.Random

class ChartRepository @Inject constructor(): IChartRepository {
    override suspend fun getChartData(): Flow<Pair<Double, Double>> {
       return DataGenerator.getRandomDoubles()
    }
}