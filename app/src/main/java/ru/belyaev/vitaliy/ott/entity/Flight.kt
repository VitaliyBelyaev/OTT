package ru.belyaev.vitaliy.ott.entity

import com.google.gson.annotations.SerializedName

data class Flight(
    @SerializedName("arrival") val arrival: String,
    @SerializedName("companyId") val companyId: Int,
    @SerializedName("departure") val departure: String,
    @SerializedName("id") val id: Int,
    @SerializedName("price") val price: Int
) {
    var companyName: String? = null
}