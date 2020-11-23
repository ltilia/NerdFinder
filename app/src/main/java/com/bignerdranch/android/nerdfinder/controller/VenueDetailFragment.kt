package com.bignerdranch.android.nerdfinder.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bignerdranch.android.nerdfinder.R
import com.bignerdranch.android.nerdfinder.model.TokenStore
import com.bignerdranch.android.nerdfinder.model.Venue

class VenueDetailFragment : Fragment() {

    private lateinit var venueId: String
    private lateinit var venue: Venue
    private lateinit var venueNameTextView: TextView
    private lateinit var venueAddressTextView: TextView
    private lateinit var checkInButton: Button
    private lateinit var tokenStore: TokenStore

    private val checkInClickListener = View.OnClickListener { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenStore = TokenStore.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
                R.layout.fragment_venue_detail, container, false)
        venueNameTextView = view.findViewById(R.id.fragment_venue_detail_venue_name_text_view)
        venueAddressTextView = view.findViewById(R.id.fragment_venue_detail_venue_address_text_view)
        checkInButton = view.findViewById(R.id.fragment_venue_detail_check_in_button)
        return view
    }

    override fun onStart() {
        super.onStart()
        venueId = requireArguments().getString(ARG_VENUE_ID)!!
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    companion object {
        private val ARG_VENUE_ID = "VenueDetailFragment.VenueId"

        fun newInstance(venueId: String): VenueDetailFragment {
            val fragment = VenueDetailFragment()

            val args = Bundle()
            args.putString(ARG_VENUE_ID, venueId)
            fragment.arguments = args

            return fragment
        }
    }
}
