package ru.belyaev.vitaliy.ott.entity.response

import com.google.gson.annotations.SerializedName
import ru.belyaev.vitaliy.ott.entity.Hotel

data class HotelsResponse(
    @SerializedName("hotels") val hotels: List<Hotel>
)