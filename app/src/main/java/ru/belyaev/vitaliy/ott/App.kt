package ru.belyaev.vitaliy.ott

import android.app.Application
import ru.belyaev.vitaliy.ott.network.NetworkModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        NetworkModule.initialize(this)
    }
}