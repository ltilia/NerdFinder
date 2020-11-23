package com.bignerdranch.android.nerdfinder.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.nerdfinder.controller.VenueDetailActivity
import com.bignerdranch.android.nerdfinder.model.Venue

class VenueHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val venueView: VenueView = itemView as VenueView
    private var venue: Venue? = null

    init {
        venueView.setOnClickListener(this)
    }

    fun bindVenue(venue: Venue) {
        this.venue = venue
        venueView.setVenueTitle(venue.name)
        venueView.setVenueAddress(venue.formattedAddress)
    }

    override fun onClick(view: View) {
        venue?.let {
            val context = view.context
            val intent = VenueDetailActivity.newIntent(context, it.id)
            context.startActivity(intent)
        }
    }
}
