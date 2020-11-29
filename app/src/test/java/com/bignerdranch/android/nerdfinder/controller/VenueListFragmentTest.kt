package com.bignerdranch.android.nerdfinder.controller

import android.app.Application
import android.os.Build
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bignerdranch.android.nerdfinder.R
import com.bignerdranch.android.nerdfinder.SynchronousExecutorService
import com.bignerdranch.android.nerdfinder.model.TokenStore
import com.bignerdranch.android.nerdfinder.model.VenueSearchResponse
import com.bignerdranch.android.nerdfinder.web.DataManager
import com.bignerdranch.android.nerdfinder.web.TestDataManager
import com.bignerdranch.android.nerdfinder.web.VenueListDeserializer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.google.gson.GsonBuilder
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import  org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.equalTo
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P], application = Application::class)
class VenueListFragmentTest {

    @get:Rule
    var wireMockRule = WireMockRule(1111)
    private val endpoint =  "http://localhost:1111/"
    private lateinit var dataManager:DataManager
    @Before
    fun setup(){
        val gson = GsonBuilder().registerTypeAdapter(VenueSearchResponse::class.java,
                VenueListDeserializer()).create()

        val executorService = SynchronousExecutorService()
        val client = OkHttpClient.Builder().dispatcher(Dispatcher(executorService)).build()
        val basicRetrofit = Retrofit.Builder().baseUrl(endpoint).client(client).
        addConverterFactory(GsonConverterFactory.create(gson)).build()

        val tokenStore = TokenStore.getInstance(ApplicationProvider.getApplicationContext())
        val authenticatedRetrofit = Retrofit.Builder().baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create(gson)).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
        dataManager = TestDataManager.getInstance(tokenStore,basicRetrofit,authenticatedRetrofit)
        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/venues/search.*"))
                .willReturn(WireMock.aResponse().withStatus(200).withBodyFile("search.json")))
    }

    @After
    fun tearDown(){
        TestDataManager.reset()
    }

    @Test
    fun activityListsVenuesReturnedFromSearch(){
        val  activityScenario =launch(VenueListActivity::class.java)
        activityScenario.onActivity { venueListActivity->
            val venueListFragment = venueListActivity.supportFragmentManager
                    .findFragmentById(R.id.fragmentContainer) as VenueListFragment

            assertThat(venueListFragment, `is`(notNullValue()))
            val venueRecyclerView = venueListFragment.view!!.
            findViewById<RecyclerView>(R.id.venueListRecyclerView)
            assertThat(venueRecyclerView,  `is`(notNullValue()))
            assertThat(venueRecyclerView.adapter!!.itemCount, `is`(2))
            var bnrTitle=  "BNR Intergalactic Headquarters"
            val rndTitle ="Ration and Dram"
            val firstVenueView = venueRecyclerView.getChildAt(0)
            val venueTitleTextView = firstVenueView.findViewById<TextView>(R.id.view_venue_list_VenueTitleTextView)
            assertThat(venueTitleTextView.text.toString(), `is`(equalTo(bnrTitle)))

            val secondVenueView = venueRecyclerView.getChildAt(1)
            val venueTitleTextView2 = secondVenueView.findViewById<TextView>(R.id.view_venue_list_VenueTitleTextView)
            assertThat(venueTitleTextView2.text.toString(),`is`(equalTo(rndTitle)))
        }

    }
}