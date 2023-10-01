package ru.dinarastepina.myapplication

import android.app.Application
import ru.dinarastepina.myapplication.di.DaggerChartComponent
class MyChartApp: Application() {

    val component by lazy {
        DaggerChartComponent.factory().create(this)
    }
}