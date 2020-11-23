package com.bignerdranch.android.nerdfinder.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.nerdfinder.model.Venue

class VenueListAdapter(_venueList: List<Venue>) : RecyclerView.Adapter<VenueHolder>() {

    var venueList = _venueList
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VenueHolder {
        val venueView = VenueView(viewGroup.context)
        return VenueHolder(venueView)
    }

    override fun onBindViewHolder(venueHolder: VenueHolder, position: Int) {
        venueHolder.bindVenue(venueList[position])
    }

    override fun getItemCount() = venueList.size
}
