package ru.belyaev.vitaliy.ott.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_flights.view.*
import ru.belyaev.vitaliy.ott.R
import ru.belyaev.vitaliy.ott.entity.Company
import ru.belyaev.vitaliy.ott.entity.Hotel
import ru.belyaev.vitaliy.ott.entity.Order
import ru.belyaev.vitaliy.ott.entity.ToursData
import ru.belyaev.vitaliy.ott.presentation.MainPresenter
import ru.belyaev.vitaliy.ott.presentation.MainView


class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    lateinit var toursAdapter: ToursAdapter
    private var flightsDialog: Dialog? = null
    private var filterDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.context = this

        val tourClickListener: (hotel: Hotel) -> Unit = {
            presenter.onHotelClick(it.id)
        }

        toursAdapter = ToursAdapter(this, tourClickListener)
        toursRV.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = toursAdapter
        }
        swipeToRefresh.setOnRefreshListener { presenter.fetchHotels() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) return super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.filter -> {
                if (toursAdapter.toursData == null) {
                    presenter.onNothingToFilter()
                    return true
                }
                presenter.onFilterMenuItemClick()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProgress(show: Boolean) {
        swipeToRefresh.isRefreshing = show
    }

    override fun showEmptyView(show: Boolean) {
        if (show) {
            emptyTV.visibility = View.VISIBLE
        } else {
            emptyTV.visibility = View.GONE
        }
    }

    override fun showTours(show: Boolean, toursData: ToursData) {
        if (show) {
            toursRV.visibility = View.VISIBLE
            toursAdapter.setData(toursData)
        } else {
            toursRV.visibility = View.INVISIBLE
        }
    }

    override fun showFlightsDialog(show: Boolean, order: Order?) {
        if (!show) {
            flightsDialog?.dismiss()
            return
        }

        val onFlightClickListener: (hotelId: Int, flightId: Int) -> Unit = { hotelId, flightId ->
            presenter.onFlightClick(hotelId, flightId)

        }

        val view = LayoutInflater.from(this).inflate(R.layout.dialog_flights, rootView, false)

        view.flightsRV.layoutManager = LinearLayoutManager(this)
        val adapter = FlightAdapter(order!!, this, onFlightClickListener)
        view.flightsRV.adapter = adapter

        flightsDialog = AlertDialog.Builder(this)
            .setView(view)
            .setOnCancelListener { presenter.onFlightsDialogCancel() }.create()

        flightsDialog!!.show()
    }

    override fun showFilterDialog(show: Boolean, companies: List<Company>?) {
        if (!show) {
            filterDialog?.dismiss()
            return
        }

        val companyNameList = companies!!.map { it.name }.toTypedArray()

        filterDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.filter_title))
            .setOnCancelListener { presenter.onFilterDialogCancel() }
            .setItems(companyNameList) { _: DialogInterface, position: Int ->
                presenter.onCompanyFilterSelect(companies[position].id)
            }
            .create()

        filterDialog!!.show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
