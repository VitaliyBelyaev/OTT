package ru.belyaev.vitaliy.ott.entity

data class Order(
    val hotel: Hotel,
    val flights: List<Flight>
)
