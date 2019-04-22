package ru.belyaev.vitaliy.ott.presentation

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.belyaev.vitaliy.ott.R
import ru.belyaev.vitaliy.ott.entity.Flight
import ru.belyaev.vitaliy.ott.entity.Hotel
import ru.belyaev.vitaliy.ott.entity.Order
import ru.belyaev.vitaliy.ott.entity.ToursData
import ru.belyaev.vitaliy.ott.entity.response.CompaniesResponse
import ru.belyaev.vitaliy.ott.entity.response.FlightsResponse
import ru.belyaev.vitaliy.ott.entity.response.HotelsResponse
import ru.belyaev.vitaliy.ott.network.NetworkModule

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    var allToursData = ToursData()
    var filteredToursData = ToursData()
    var filterCompanyId = -1
    lateinit var context: Context

    fun onHotelClick(hotelId: Int) {
        val toursData = if (filterCompanyId != -1) {
            filteredToursData
        } else {
            allToursData
        }

        val hotel = toursData.hotels.find { it.id == hotelId }!!
        val flights = mutableListOf<Flight>()

        for (flightId in hotel.flights) {
            val flight = toursData.flights.find { it.id == flightId }
            val companyName = toursData.companies.find { it.id == flight!!.companyId }!!.name
            flight!!.companyName = companyName
            flights.add(flight)
        }

        if (hotel.flights.size == 1) {
            val message = context.getString(R.string.selection_message, hotel.name, hotel.price + flights[0].price)
            viewState.showMessage(message)
            return
        }

        val order = Order(hotel, flights)
        viewState.showFlightsDialog(true, order)
    }

    fun onFlightClick(hotelId: Int, flightId: Int) {
        val hotel = allToursData.hotels.find { it.id == hotelId }
        val flight = allToursData.flights.find { it.id == flightId }

        val message = context.getString(R.string.selection_message, hotel!!.name, hotel.price + flight!!.price)
        viewState.showFlightsDialog(false, null)
        viewState.showMessage(message)
    }

    fun onCompanyFilterSelect(companyId: Int) {
        filterCompanyId = companyId

        val companyFlights = allToursData.flights.filter { it.companyId == companyId }

        val clonedHotels = mutableListOf<Hotel>()
        for (oHotel in allToursData.hotels) {
            clonedHotels.add(oHotel.copy())
        }

        val fHotels = clonedHotels.filter { hotel ->
            hotel.flights.any { it in companyFlights.map { it.id } }
        }

        for (hotel in fHotels) {
            hotel.flights = hotel.flights.filter { it in companyFlights.map { flight -> flight.id } }
        }

        filteredToursData.apply {
            hotels = fHotels
            flights = allToursData.flights
            companies = allToursData.companies
        }

        viewState.showFilterDialog(false, null)
        viewState.showTours(true, filteredToursData)
    }

    fun onFilterMenuItemClick() {
        viewState.showFilterDialog(true, allToursData.companies)
    }

    fun onNothingToFilter() {
        viewState.showMessage(context.getString(R.string.nothing_to_filter))
    }

    fun onFilterDialogCancel() {
        viewState.showFilterDialog(false, null)
    }

    fun onFlightsDialogCancel() {
        viewState.showFlightsDialog(false, null)
    }

    fun fetchHotels() {
        viewState.showProgress(true)

        NetworkModule.api.getHotels().enqueue(object : Callback<HotelsResponse> {
            override fun onFailure(call: Call<HotelsResponse>, t: Throwable) {
                viewState.showMessage(context.getString(R.string.fetching_error_message))
            }

            override fun onResponse(call: Call<HotelsResponse>, response: Response<HotelsResponse>) {
                if (!response.isSuccessful || response.body() == null) {
                    viewState.showMessage(context.getString(R.string.fetching_error_message))
                    return
                }

                allToursData.hotels = response.body()!!.hotels

                fetchFlights()
            }
        })
    }

    private fun fetchFlights() {
        NetworkModule.api.getFlights().enqueue(object : Callback<FlightsResponse> {
            override fun onFailure(call: Call<FlightsResponse>, t: Throwable) {
                viewState.showMessage(context.getString(R.string.fetching_error_message))
            }

            override fun onResponse(call: Call<FlightsResponse>, response: Response<FlightsResponse>) {
                if (!response.isSuccessful || response.body() == null) {
                    viewState.showMessage(context.getString(R.string.fetching_error_message))
                    return
                }

                allToursData.flights = response.body()!!.flights

                fetchCompanies()
            }
        })
    }

    private fun fetchCompanies() {
        NetworkModule.api.getCompanies().enqueue(object : Callback<CompaniesResponse> {
            override fun onFailure(call: Call<CompaniesResponse>, t: Throwable) {
                viewState.showMessage(context.getString(R.string.fetching_error_message))
            }

            override fun onResponse(call: Call<CompaniesResponse>, response: Response<CompaniesResponse>) {
                if (!response.isSuccessful || response.body() == null) {
                    viewState.showMessage(context.getString(R.string.fetching_error_message))
                    return
                }

                allToursData.companies = response.body()!!.companies

                filterCompanyId = -1
                viewState.showProgress(false)
                viewState.showEmptyView(false)
                viewState.showTours(true, allToursData)
            }
        })
    }
}