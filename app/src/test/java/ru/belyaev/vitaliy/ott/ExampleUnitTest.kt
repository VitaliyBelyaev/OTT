package ru.belyaev.vitaliy.ott

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.belyaev.vitaliy.ott.entity.Company
import ru.belyaev.vitaliy.ott.entity.Flight
import ru.belyaev.vitaliy.ott.entity.Hotel
import ru.belyaev.vitaliy.ott.entity.ToursData
import ru.belyaev.vitaliy.ott.presentation.MainPresenter

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    val hotelsJson =
        "[{\"id\":0,\"flights\":[0,7,10],\"name\":\"Шале у Петровича\",\"price\":2000},{\"id\":1,\"flights\":[1,4,14],\"name\":\"Пирамида\",\"price\":2500},{\"id\":2,\"flights\":[2,4],\"name\":\"Ride and sleep\",\"price\":3200},{\"id\":3,\"flights\":[3],\"name\":\"Бизнес-отель\",\"price\":14700},{\"id\":4,\"flights\":[5,8,11,19],\"name\":\"Звездная ночь\",\"price\":6500}]"
    val flightsJson =
        "[{\"id\":0,\"companyId\":0,\"departure\":\"12:00\",\"arrival\":\"15:00\",\"price\":3000},{\"id\":1,\"companyId\":1,\"departure\":\"14:00\",\"arrival\":\"17:00\",\"price\":5000},{\"id\":2,\"companyId\":1,\"departure\":\"14:00\",\"arrival\":\"17:00\",\"price\":3300},{\"id\":3,\"companyId\":2,\"departure\":\"14:00\",\"arrival\":\"17:00\",\"price\":4000},{\"id\":4,\"companyId\":3,\"departure\":\"14:00\",\"arrival\":\"17:00\",\"price\":3990},{\"id\":5,\"companyId\":0,\"departure\":\"14:00\",\"arrival\":\"17:00\",\"price\":4560},{\"id\":6,\"companyId\":2,\"departure\":\"14:00\",\"arrival\":\"17:00\",\"price\":9400}]"
    val companiesJson =
        "[{\"id\":0,\"name\":\"S7\"},{\"id\":1,\"name\":\"Aeroflot\"},{\"id\":2,\"name\":\"Swiss\"},{\"id\":3,\"name\":\"Pobeda\"},{\"id\":4,\"name\":\"Lufthansa\"}]"


    val hotelsListType = object : TypeToken<List<Hotel>>() {}.type
    val oHotels: List<Hotel> = Gson().fromJson(hotelsJson, hotelsListType)

    val flightsListType = object : TypeToken<List<Flight>>() {}.type
    val oFlights: List<Flight> = Gson().fromJson(flightsJson, flightsListType)

    val companiesListType = object : TypeToken<List<Company>>() {}.type
    val oCompanies: List<Company> = Gson().fromJson(companiesJson, companiesListType)

    @Test
    fun firstFilteringIsCorrect() {
        val presenter = MainPresenter()

        //original data initialization
        presenter.allToursData.apply {
            this.hotels = oHotels
            this.flights = oFlights
            this.companies = oCompanies
        }

        //filter logic calling
        presenter.onCompanyFilterSelect(0)

        val clonedHotels = mutableListOf<Hotel>()
        for (oHotel in oHotels) {
            clonedHotels.add(oHotel.copy())
        }
        clonedHotels[0].flights = listOf(0)
        clonedHotels[4].flights = listOf(5)
        val rightFilteredHotels = listOf(clonedHotels[0], clonedHotels[4])

        assertEquals("Hotels lists must be the same", rightFilteredHotels, presenter.filteredToursData.hotels)
    }

    @Test
    fun filteringAfterPreviousFilter() {
        val presenter = MainPresenter()

        //original data initialization
        presenter.allToursData.apply {
            this.hotels = oHotels
            this.flights = oFlights
            this.companies = oCompanies
        }
        //filter logic calling
        presenter.onCompanyFilterSelect(3)
        presenter.onCompanyFilterSelect(0)


        val clonedHotels = mutableListOf<Hotel>()
        for (oHotel in oHotels) {
            clonedHotels.add(oHotel.copy())
        }
        clonedHotels[0].flights = listOf(0)
        clonedHotels[4].flights = listOf(5)
        val rightFilteredHotels = listOf(clonedHotels[0], clonedHotels[4])

        assertEquals("Hotels lists must be the same", rightFilteredHotels, presenter.filteredToursData.hotels)
    }

    @Test
    fun originalToursDataStayNotChanged() {
        val clonedHotels = mutableListOf<Hotel>()
        val clonedFlights = mutableListOf<Flight>()
        val clonedCompanies = mutableListOf<Company>()

        for (oHotel in oHotels) {
            clonedHotels.add(oHotel.copy())
        }
        for (oFlight in oFlights) {
            clonedFlights.add(oFlight.copy())
        }
        for (oCompany in oCompanies) {
            clonedCompanies.add(oCompany.copy())
        }

        val snapshotOfOriginalToursData = ToursData().apply {
            this.hotels = clonedHotels
            this.flights = clonedFlights
            this.companies = clonedCompanies
        }

        val presenter = MainPresenter()

        //original data initialization
        presenter.allToursData.apply {
            this.hotels = oHotels
            this.flights = oFlights
            this.companies = oCompanies
        }
        //filter logic calling
        presenter.onCompanyFilterSelect(2)

        assertEquals("AllToursData must stay not changed", snapshotOfOriginalToursData, presenter.allToursData)
    }
}
