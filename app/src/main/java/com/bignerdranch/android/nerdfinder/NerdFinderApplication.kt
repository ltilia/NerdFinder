package com.bignerdranch.android.nerdfinder

import android.app.Application
import com.bignerdranch.android.nerdfinder.web.DataManager

class NerdFinderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DataManager.initialize(this)
    }
}
