package ru.belyaev.vitaliy.ott.entity.response

import com.google.gson.annotations.SerializedName
import ru.belyaev.vitaliy.ott.entity.Flight

data class FlightsResponse(
    @SerializedName("flights") val flights: List<Flight>
)