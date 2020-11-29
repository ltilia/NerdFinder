package com.bignerdranch.android.nerdfinder.web

import com.bignerdranch.android.nerdfinder.model.VenueSearchResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface VenueInterface {
    @GET("venues/search")
    fun venueSearch(@Query("ll") latLngString: String): Call<VenueSearchResponse>

    @FormUrlEncoded
    @POST("checkins/add")
    fun venueCheckIn(@Field("venueId") venueId: String):Observable<Any>
}
