package ru.belyaev.vitaliy.ott.network

import retrofit2.Call
import retrofit2.http.GET
import ru.belyaev.vitaliy.ott.entity.response.CompaniesResponse
import ru.belyaev.vitaliy.ott.entity.response.FlightsResponse
import ru.belyaev.vitaliy.ott.entity.response.HotelsResponse


interface Api {
    @GET("bins/zqxvw")
    fun getFlights(): Call<FlightsResponse>

    @GET("bins/12q3ws")
    fun getHotels(): Call<HotelsResponse>

    @GET("bins/8d024")
    fun getCompanies(): Call<CompaniesResponse>
}