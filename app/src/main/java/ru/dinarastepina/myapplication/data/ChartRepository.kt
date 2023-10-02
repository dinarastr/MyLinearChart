package ru.dinarastepina.myapplication.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.dinarastepina.myapplication.domain.IChartRepository
import javax.inject.Inject
import kotlin.random.Random

class ChartRepository @Inject constructor(): IChartRepository {
    override suspend fun getChartData(): Flow<Pair<Double, Double>> {
        var seed = Random.nextInt(-1, 2)
        var time = 0.0
        return flow {
            while(true) {
                emit(seed.toDouble() to time)
                val next = Random.nextInt(-1, 2)
                println(seed)
                seed += next
                time++
                delay(1000)
            }
        }
    }
}