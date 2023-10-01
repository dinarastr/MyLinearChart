package ru.dinarastepina.myapplication.di

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.dinarastepina.myapplication.presentation.linearChart.LinearChartVM
import kotlin.reflect.KClass

@Module
interface VMModule {

    @Binds
    @IntoMap
    @ViewModelKey(LinearChartVM::class)
    fun bindLinearChartVM(linearChartVM: LinearChartVM): LinearChartVM
}