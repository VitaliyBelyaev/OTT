package ru.belyaev.vitaliy.ott.entity

class ToursData {
    lateinit var hotels: List<Hotel>
    lateinit var flights: List<Flight>
    lateinit var companies: List<Company>

    override fun toString(): String {
        return "ToursData(hotels=$hotels, flights=$flights, companies=$companies)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ToursData

        if (hotels != other.hotels) return false
        if (flights != other.flights) return false
        if (companies != other.companies) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hotels.hashCode()
        result = 31 * result + flights.hashCode()
        result = 31 * result + companies.hashCode()
        return result
    }


}