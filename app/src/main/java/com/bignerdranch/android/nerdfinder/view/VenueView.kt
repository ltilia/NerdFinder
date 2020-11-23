package com.bignerdranch.android.nerdfinder.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bignerdranch.android.nerdfinder.R

class VenueView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {

    private var titleTextView: TextView
    private val addressTextView: TextView

    init {
        orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, 0, 0, 16)
        layoutParams = params

        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(
                R.layout.view_venue, this, true) as VenueView

        titleTextView = view.findViewById(
                R.id.view_venue_list_VenueTitleTextView) as TextView

        addressTextView = view.findViewById(
                R.id.view_venue_list_VenueLocationTextView) as TextView
    }

    fun setVenueTitle(title: String) {
        titleTextView.text = title
    }

    fun setVenueAddress(address: String) {
        addressTextView.text = address
    }
}
