package ru.belyaev.vitaliy.ott.presentation

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.belyaev.vitaliy.ott.R
import ru.belyaev.vitaliy.ott.entity.Flight
import ru.belyaev.vitaliy.ott.entity.Order
import ru.belyaev.vitaliy.ott.entity.ToursData
import ru.belyaev.vitaliy.ott.entity.response.CompaniesResponse
import ru.belyaev.vitaliy.ott.entity.response.FlightsResponse
import ru.belyaev.vitaliy.ott.entity.response.HotelsResponse
import ru.belyaev.vitaliy.ott.network.NetworkModule

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    var toursData = ToursData()
    lateinit var context: Context

    fun onHotelClick(hotelId: Int) {

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
        val hotel = toursData.hotels.find { it.id == hotelId }
        val flight = toursData.flights.find { it.id == flightId }

        val message = context.getString(R.string.selection_message, hotel!!.name, hotel.price + flight!!.price)
        viewState.showFlightsDialog(false, null)
        viewState.showMessage(message)
    }

    fun onCompanyFilterSelect(companyId: Int) {

        val companyFlights = toursData.flights.filter { it.companyId == companyId }
        val filteredHotels = toursData.hotels.filter { hotel ->
            hotel.flights.any { it in companyFlights.map { it.id } }
        }

        for (hotel in filteredHotels) {
            hotel.flights = hotel.flights.filter { it in companyFlights.map { flight -> flight.id } }
        }

        val newToursData = ToursData().apply {
            hotels = filteredHotels
            flights = toursData.flights
            companies = toursData.companies
        }

        viewState.showFilterDialog(false, null)
        viewState.showTours(true, newToursData)
    }

    fun onFilterMenuItemClick() {
        viewState.showFilterDialog(true, toursData.companies)
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

                toursData.hotels = response.body()!!.hotels

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

                toursData.flights = response.body()!!.flights

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

                toursData.companies = response.body()!!.companies

                viewState.showProgress(false)
                viewState.showEmptyView(false)
                viewState.showTours(true, toursData)
            }
        })
    }
}