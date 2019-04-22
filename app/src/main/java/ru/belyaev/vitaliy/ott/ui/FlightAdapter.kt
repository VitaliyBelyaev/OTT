package ru.belyaev.vitaliy.ott.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_flight.view.*
import ru.belyaev.vitaliy.ott.R
import ru.belyaev.vitaliy.ott.entity.Flight
import ru.belyaev.vitaliy.ott.entity.Order

class FlightAdapter(
    private val order: Order,
    private val context: Context,
    private val onClickListener: (hotelId: Int, flightId: Int) -> Unit
) : RecyclerView.Adapter<FlightAdapter.FlightVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_flight, parent, false)
        return FlightVH(itemView)
    }

    override fun getItemCount(): Int {
        return order.flights.size
    }

    override fun onBindViewHolder(holder: FlightVH, position: Int) {
        holder.bind(order.flights[position])
    }

    inner class FlightVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val companyName = itemView.companyNameTV
        private val price = itemView.flightPriceTV

        fun bind(flight: Flight) {
            companyName.text = flight.companyName

            val totalPrice = order.hotel.price + flight.price
            price.text = context.getString(R.string.tour_price_flights_dialog, totalPrice)

            itemView.setOnClickListener {
                onClickListener.invoke(order.hotel.id, flight.id)
            }
        }
    }
}