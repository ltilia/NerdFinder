package com.bignerdranch.android.nerdfinder.controller

import android.app.Application
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bignerdranch.android.nerdfinder.web.DataManager
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P], application = Application::class)
class VenueDetailFragmentTest {

    @get:Rule
    var wireMockRule= WireMockRule(111)
    private val endpoint ="http://localhost:1111/"
    private lateinit var dataManager:DataManager
}