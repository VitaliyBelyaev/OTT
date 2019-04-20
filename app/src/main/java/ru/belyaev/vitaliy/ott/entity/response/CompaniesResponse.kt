package ru.belyaev.vitaliy.ott.entity.response

import com.google.gson.annotations.SerializedName
import ru.belyaev.vitaliy.ott.entity.Company

data class CompaniesResponse(
    @SerializedName("companies") val companies: List<Company>
)