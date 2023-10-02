package ru.dinarastepina.myapplication.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChartDataInteractor @Inject constructor(
    private val repository: IChartRepository
) {
    suspend operator fun invoke(): Flow<Double> {
        return repository.getChartData()
    }
}