package ru.belyaev.vitaliy.ott

import android.app.Application
import ru.belyaev.vitaliy.ott.network.Api
import ru.belyaev.vitaliy.ott.network.NetworkModule

class App : Application() {

    lateinit var api: Api

    override fun onCreate() {
        super.onCreate()

        val networkModule = NetworkModule(this)
        api = networkModule.api
    }
}