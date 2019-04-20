package ru.belyaev.vitaliy.ott.network

import android.content.Context
import android.util.Log
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


class NetworkModule(context: Context) {

    companion object {
        const val API_HOST = "https://api.myjson.com/"
        private const val HTTP_CACHE_SIZE = 1024 * 1024 * 24L // 24 MB
    }

    val api: Api

    init {
        val okHttpClient = provideOkHttp(context.cacheDir)
        val retrofit = provideRetrofit(okHttpClient, API_HOST)
        api = retrofit.create(Api::class.java)
    }

    private fun provideOkHttp(cacheDir: File): OkHttpClient {
        val logger = HttpLoggingInterceptor.Logger { message -> Log.d("OKHTTP", message) }

        val loggingInterceptor = HttpLoggingInterceptor(logger)
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)

        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .cache(Cache(File(cacheDir, "http"), HTTP_CACHE_SIZE))
            .build()

    }

    private fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
}