package ru.dinarastepina.myapplication.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

const val TIME_INTERVAL: Long = 1000
class DataGenerator {
    companion object {
        fun getRandomDoubles(fifoCapacity: Int = 0): Flow<Pair<Double, Double>> {
            var seed = Random.nextInt(-1, 2)
            var time = 0.0
            return flow {
                while(true) {
                    emit(seed.toDouble() to time)
                    val next = Random.nextInt(-1, 2)
                    seed += next
                    time++
                    delay(TIME_INTERVAL)
                }
            }
        }
    }
}