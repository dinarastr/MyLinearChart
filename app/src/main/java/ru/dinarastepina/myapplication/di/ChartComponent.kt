package ru.dinarastepina.myapplication.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.dinarastepina.myapplication.presentation.linearChart.LinearChartFragment


@ChartScope
@Component(modules = [DataModule::class, VMModule::class])
interface ChartComponent {

    fun inject(fragment: LinearChartFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ChartComponent
    }
}