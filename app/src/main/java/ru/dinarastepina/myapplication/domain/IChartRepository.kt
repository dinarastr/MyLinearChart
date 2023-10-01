package ru.dinarastepina.myapplication.domain

import kotlinx.coroutines.flow.Flow

interface IChartRepository {
    suspend fun getChartData(): Flow<Double>
}