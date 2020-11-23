package com.bignerdranch.android.nerdfinder.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.nerdfinder.R

class VenueListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_list)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, VenueListFragment.newInstance())
                    .commit()
        }
    }
}
