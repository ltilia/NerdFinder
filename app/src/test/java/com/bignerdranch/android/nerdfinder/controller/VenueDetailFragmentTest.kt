package com.bignerdranch.android.nerdfinder.controller

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bignerdranch.android.nerdfinder.R
import com.bignerdranch.android.nerdfinder.SynchronousExecutorService
import com.bignerdranch.android.nerdfinder.exception.AuthorizationInterceptor
import com.bignerdranch.android.nerdfinder.model.TokenStore
import com.bignerdranch.android.nerdfinder.model.VenueSearchResponse
import com.bignerdranch.android.nerdfinder.web.DataManager
import com.bignerdranch.android.nerdfinder.web.TestDataManager
import com.bignerdranch.android.nerdfinder.web.VenueListDeserializer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.google.gson.GsonBuilder
import java.util.concurrent.ExecutorService
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.*
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowLooper
import org.robolectric.shadows.ShadowToast
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P], application = Application::class)
class VenueDetailFragmentTest {

    @get:Rule
    var wireMockRule= WireMockRule(111)
    private val endpoint ="http://localhost:1111/"
    private lateinit var dataManager:DataManager

    @Before
    fun setup(){
        val gson = GsonBuilder().registerTypeAdapter(
                VenueSearchResponse::class.java,VenueListDeserializer()).create()

        val executorService:ExecutorService = SynchronousExecutorService()
        val client = OkHttpClient.Builder().dispatcher(Dispatcher(executorService)).build()
        val basicRetrofit = Retrofit.Builder().baseUrl(endpoint).client(client).
        addConverterFactory(GsonConverterFactory.create()).build()
        val authenticatedClient = OkHttpClient.Builder().dispatcher(Dispatcher(executorService))
                .addInterceptor(AuthorizationInterceptor()).build()

        val authenticatedRetrofit = Retrofit.Builder().baseUrl(endpoint).client(authenticatedClient).
                addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()

        val tokenStore:TokenStore = TokenStore.getInstance(ApplicationProvider.getApplicationContext())
        tokenStore.accessToken = "bogus token for testing"
        dataManager = TestDataManager.getInstance(tokenStore, basicRetrofit, authenticatedRetrofit)
        stubFor(get(urlMatching("/venues/search.*")).willReturn(aResponse().withStatus(200)
                .withBodyFile("search.json")))

        dataManager.fetchVenueSearch()
    }

    @After
    fun tearDown(){
        TestDataManager.reset()
    }
    @Test
    fun toastShownOnSuccessfulCheckIn() {
        stubFor(
                post(urlMatching("/checkins/add.*"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody("{}")
                        )
        )
        val bnrVenueId = "527c1d4f11d20f41ba39fc01"
        val detailIntent =
                VenueDetailActivity.newIntent(ApplicationProvider.getApplicationContext(), bnrVenueId)
        val activityScenario = launch<VenueDetailActivity>(detailIntent)
        activityScenario.onActivity { venueDetailActivity ->
            onView(withId(R.id.fragment_venue_detail_check_in_button)).perform(click())
            ShadowLooper.idleMainLooper()
            val expectedToastText =
                    venueDetailActivity.getString(R.string.successful_check_in_message)
            assertThat(
                    ShadowToast.getTextOfLatestToast(),
                    `is`(CoreMatchers.equalTo(expectedToastText))
            )
        }
    }

    @Test
    fun errorDialogShownOnUnauthorizedException() {
        stubFor(
                post(urlMatching("/checkins/add.*"))
                        .willReturn(
                                aResponse()
                                        .withStatus(401)
                        )
        )
        val bnrVenueId = "527c1d4f11d20f41ba39fc01"
        val detailIntent = VenueDetailActivity
                .newIntent(ApplicationProvider.getApplicationContext(), bnrVenueId)
        val activityScenario = launch<VenueDetailActivity>(detailIntent)
        activityScenario.onActivity { venueDetailActivity ->
            onView(withId(R.id.fragment_venue_detail_check_in_button))
                    .perform(click())
            ShadowLooper.idleMainLooper()
            val errorDialog = ShadowAlertDialog.getLatestAlertDialog()
            assertThat(errorDialog, `is`(notNullValue()))
            val alertDialog: ShadowAlertDialog = shadowOf(errorDialog)
            val expectedDialogTitle =
                    venueDetailActivity.getString(R.string.expired_token_dialog_title)
            val expectedDialogMessage =
                    venueDetailActivity.getString(R.string.expired_token_dialog_message)
            assertThat(alertDialog.title.toString(), `is`(equalTo(expectedDialogTitle)))
            assertThat(alertDialog.message.toString(), `is`(equalTo(expectedDialogMessage)))
        }
    }
}