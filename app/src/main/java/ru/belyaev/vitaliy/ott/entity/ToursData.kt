package ru.belyaev.vitaliy.ott.entity

class ToursData {
    lateinit var hotels: List<Hotel>
    lateinit var flights: List<Flight>
    lateinit var companies: List<Company>

    override fun toString(): String {
        return "ToursData(hotels=$hotels, flights=$flights, companies=$companies)"
    }


}