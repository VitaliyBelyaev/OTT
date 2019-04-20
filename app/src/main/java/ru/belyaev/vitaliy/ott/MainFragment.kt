package ru.belyaev.vitaliy.ott

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.belyaev.vitaliy.ott.entity.ToursData
import ru.belyaev.vitaliy.ott.entity.response.CompaniesResponse
import ru.belyaev.vitaliy.ott.entity.response.FlightsResponse
import ru.belyaev.vitaliy.ott.entity.response.HotelsResponse

class MainFragment : Fragment() {

    lateinit var toursAdapter: ToursAdapter
    var toursData = ToursData()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tourClickListener: (Int) -> Unit = {
            Toast.makeText(context!!, "Tour clicked", LENGTH_SHORT).show()
        }

        toursRV.layoutManager = LinearLayoutManager(context)
        toursAdapter = ToursAdapter(context!!, tourClickListener)
        toursRV.adapter = toursAdapter

        fetchHotels()
    }

    private fun fetchHotels() {
        progressBar.visibility = VISIBLE
        toursRV.visibility = INVISIBLE

        getApp().api.getHotels().enqueue(object : Callback<HotelsResponse> {
            override fun onFailure(call: Call<HotelsResponse>, t: Throwable) {
                Toast.makeText(context!!, "Error while getting hotels", LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<HotelsResponse>, response: Response<HotelsResponse>) {
                if (!response.isSuccessful || response.body() == null) {
                    Toast.makeText(context!!, "Error while getting hotels", LENGTH_SHORT).show()
                    return
                }

                toursData.hotels = response.body()!!.hotels

                fetchFlights()
            }

        })
    }

    private fun fetchFlights() {
        getApp().api.getFlights().enqueue(object : Callback<FlightsResponse> {
            override fun onFailure(call: Call<FlightsResponse>, t: Throwable) {
                Toast.makeText(context!!, "Error while getting flights", LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<FlightsResponse>, response: Response<FlightsResponse>) {
                if (!response.isSuccessful || response.body() == null) {
                    Toast.makeText(context!!, "Error while getting flights", LENGTH_SHORT).show()
                    return
                }

                toursData.flights = response.body()!!.flights

                fetchCompanies()
            }
        })
    }

    private fun fetchCompanies() {
        getApp().api.getCompanies().enqueue(object : Callback<CompaniesResponse> {
            override fun onFailure(call: Call<CompaniesResponse>, t: Throwable) {
                Toast.makeText(context!!, "Error while getting companies", LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<CompaniesResponse>, response: Response<CompaniesResponse>) {
                if (!response.isSuccessful || response.body() == null) {
                    Toast.makeText(context!!, "Error while getting companies", LENGTH_SHORT).show()
                    return
                }

                toursData.companies = response.body()!!.companies

                toursAdapter.setData(toursData)

                progressBar.visibility = INVISIBLE
                toursRV.visibility = VISIBLE
            }
        })
    }


    private fun getApp(): App {
        return activity!!.application as App
    }
}