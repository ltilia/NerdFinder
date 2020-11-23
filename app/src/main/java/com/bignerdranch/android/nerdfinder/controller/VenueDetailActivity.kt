package com.bignerdranch.android.nerdfinder.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.nerdfinder.R

class VenueDetailActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_detail)

        val venueId = intent.getStringExtra(ARG_VENUE_ID)!!
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, VenueDetailFragment.newInstance(venueId))
                    .commit()
        }
    }

    companion object {
        private const val ARG_VENUE_ID = "VenueDetailActivity.VenueId"

        fun newIntent(context: Context, venueId: String): Intent {
            val intent = Intent(context, VenueDetailActivity::class.java)
            intent.putExtra(ARG_VENUE_ID, venueId)
            return intent
        }
    }
}
