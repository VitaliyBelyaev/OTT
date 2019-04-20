package ru.belyaev.vitaliy.ott.entity

import com.google.gson.annotations.SerializedName

data class Hotel(
    @SerializedName("flights") val flights: List<Int>,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Int
)