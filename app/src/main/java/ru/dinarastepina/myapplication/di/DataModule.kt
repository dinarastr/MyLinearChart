package ru.dinarastepina.myapplication.di

import dagger.Binds
import dagger.Module
import ru.dinarastepina.myapplication.data.ChartRepository
import ru.dinarastepina.myapplication.domain.IChartRepository

@Module
interface DataModule {

    @Binds
    @ChartScope
    fun bindChartRepository(chartRepository: ChartRepository): IChartRepository
}