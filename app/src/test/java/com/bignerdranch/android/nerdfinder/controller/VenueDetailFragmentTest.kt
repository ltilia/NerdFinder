package com.bignerdranch.android.nerdfinder.controller

import android.app.Application
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bignerdranch.android.nerdfinder.SynchronousExecutorService
import com.bignerdranch.android.nerdfinder.exception.AuthorizationInterceptor
import com.bignerdranch.android.nerdfinder.model.TokenStore
import com.bignerdranch.android.nerdfinder.model.VenueSearchResponse
import com.bignerdranch.android.nerdfinder.web.DataManager
import com.bignerdranch.android.nerdfinder.web.TestDataManager
import com.bignerdranch.android.nerdfinder.web.VenueListDeserializer
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.google.gson.GsonBuilder
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.ExecutorService

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
    }

    @After
    fun tearDown(){
        TestDataManager.reset()
    }
}