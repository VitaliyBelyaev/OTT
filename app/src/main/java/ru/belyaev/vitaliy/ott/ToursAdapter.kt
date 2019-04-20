package ru.belyaev.vitaliy.ott

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_tour.view.*
import ru.belyaev.vitaliy.ott.entity.Hotel
import ru.belyaev.vitaliy.ott.entity.ToursData

class ToursAdapter(
    private val context: Context,
    private val onClickListener: (Int) -> Unit
) : RecyclerView.Adapter<ToursAdapter.TourVH>() {

    var toursData: ToursData? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_tour, parent, false)
        return TourVH(itemView)
    }

    override fun getItemCount(): Int {
        return toursData?.hotels?.size ?: 0
    }

    override fun onBindViewHolder(holder: TourVH, position: Int) {
        holder.bind(toursData!!.hotels[position])
    }

    fun setData(toursData: ToursData) {
        this.toursData = toursData
        notifyDataSetChanged()
    }

    inner class TourVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hotelName = itemView.hotelNameTV
        private val flightsInfo = itemView.flightsTV
        private val price = itemView.priceTV

        fun bind(hotel: Hotel) {
            hotelName.text = hotel.name

            val flightNumber = hotel.flights.size

            flightsInfo.text = when (flightNumber) {
                1 -> context.getString(R.string.one_flight_pattern, flightNumber)
                in 2..4 -> context.getString(R.string.two_four_flight_pattern, flightNumber)
                else -> context.getString(R.string.five_more_flight_pattern, flightNumber)
            }

            val hotelFlights = toursData!!.flights.filter { it.id in hotel.flights }.sortedBy { it.price }

            val minimumTotalPrice = hotel.price + hotelFlights[0].price
            price.text = context.getString(R.string.price, minimumTotalPrice)

            itemView.setOnClickListener {
                onClickListener.invoke(hotel.id)
            }
        }
    }
}