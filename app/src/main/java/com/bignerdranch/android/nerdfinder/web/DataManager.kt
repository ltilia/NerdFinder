package com.bignerdranch.android.nerdfinder.web

import android.content.Context
import android.net.Uri
import android.util.Log
import com.bignerdranch.android.nerdfinder.listener.VenueSearchListener
import com.bignerdranch.android.nerdfinder.model.TokenStore
import com.bignerdranch.android.nerdfinder.model.Venue
import com.bignerdranch.android.nerdfinder.model.VenueSearchResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataManager private constructor(
    private val tokenStore: TokenStore,
    private val retrofit: Retrofit
) {

    var venueList = emptyList<Venue>()
        private set

    private val searchListenerList = mutableListOf<VenueSearchListener>()

    fun fetchVenueSearch() {
        val venueInterface: VenueInterface = retrofit.create(VenueInterface::class.java)
        venueInterface.venueSearch(TEST_LAT_LNG)
                .enqueue(object : Callback<VenueSearchResponse> {
                    override fun onResponse(
                        call: Call<VenueSearchResponse>,
                        response: Response<VenueSearchResponse>
                    ) {
                        venueList = response.body()?.venues ?: emptyList()
                        notifySearchListeners()
                    }

                    override fun onFailure(call: Call<VenueSearchResponse>, t: Throwable) {
                        Log.e(TAG, "Failed to fetch venue search", t)
                    }
                })
    }

    fun getVenue(venueId:String):Venue? = venueList.find { it.id == venueId }

    fun addVenueSearchListener(listener: VenueSearchListener) {
        searchListenerList += listener
    }

    fun removeVenueSearchListener(listener: VenueSearchListener) {
        searchListenerList -= listener
    }

    private fun notifySearchListeners() {
        for (listener in searchListenerList) {
            listener.onVenueSearchFinished()
        }
    }

    companion object {
        private const val TAG = "DataManager"
        private const val FOURSQUARE_ENDPOINT = "https://api.foursquare.com/v2/"
        private const val CLIENT_ID = "0S2NZVLDJLAEL5HJW2TRT23EFB43UAWMOBWUTOL2E0RJDXLY"
        private const val CLIENT_SECRET = "1WDIOYRJNGKHDN5EOCU04AYABEQOR55VWTAVVMZSNGMWXVEL"
        private const val FOURSQUARE_VERSION = "20150406"
        private const val FOURSQUARE_MODE = "foursquare"
        private const val TEST_LAT_LNG = "33.759,-84.332"
        private const val OAUTH_ENDPOINT = "https://foursquare.com/oauth2/authenticate"
        const val OAUTH_REDIRECT_URI =  "https://www.bignerdranch.com"
        private var dataManager: DataManager? = null
        private lateinit var tokenStore: TokenStore

        fun initialize(context: Context) {
            if (dataManager == null) {
                val gson: Gson = GsonBuilder()
                        .registerTypeAdapter(
                                VenueSearchResponse::class.java,
                                VenueListDeserializer())
                        .create()

                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

                val client = OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(requestInterceptor)
                        .build()

                val retrofit = Retrofit.Builder()
                        .baseUrl(FOURSQUARE_ENDPOINT)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()

                tokenStore = TokenStore.getInstance(context)
                dataManager = DataManager(tokenStore, retrofit)
            }
        }

        fun get(): DataManager {
            return dataManager
            ?: throw IllegalStateException("DataManager must be initialized")
        }

        private val requestInterceptor: Interceptor = Interceptor { chain ->
            val url: HttpUrl = chain.request().url().newBuilder()
                    .addQueryParameter("client_id", CLIENT_ID)
                    .addQueryParameter("client_secret", CLIENT_SECRET)
                    .addQueryParameter("v", FOURSQUARE_VERSION)
                    .addQueryParameter("m", FOURSQUARE_MODE)
                    .build()

            val request: Request = chain.request().newBuilder()
                    .url(url)
                    .build()

            chain.proceed(request)
        }
    }

    fun getAuthenticationUrl():String?{
        return Uri.parse(OAUTH_ENDPOINT.trim()).
        buildUpon().appendQueryParameter("client_id", CLIENT_ID.trim())
                .appendQueryParameter("response_type", "token")
                .appendQueryParameter("redirect_uri", OAUTH_REDIRECT_URI.trim())
                .build().toString()
    }
}
