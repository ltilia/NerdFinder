package com.bignerdranch.android.nerdfinder.web

import com.bignerdranch.android.nerdfinder.exception.UnauthorizedException
import com.bignerdranch.android.nerdfinder.listener.VenueCheckInListener
import com.bignerdranch.android.nerdfinder.listener.VenueSearchListener
import com.bignerdranch.android.nerdfinder.model.TokenStore
import com.bignerdranch.android.nerdfinder.model.Venue
import com.bignerdranch.android.nerdfinder.model.VenueSearchResponse
import io.reactivex.Observable
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.*
import org.mockito.Mockito.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.hamcrest.CoreMatchers.`is`
import retrofit2.Retrofit
import java.util.*

inline fun <reified T:Any> mock() = Mockito.mock(T::class.java)
@RunWith(JUnit4::class)
class DataManagerTest {
    private lateinit var dataManager: DataManager

    @Captor
    private lateinit var searchCaptor:ArgumentCaptor<Callback<VenueSearchResponse>>
    @Mock
    private lateinit var retrofit: Retrofit
    @Mock
    private lateinit var authenticatedRetrofit:Retrofit
    @Mock
    private lateinit var tokenStore:TokenStore
    @Mock
    private lateinit var venueInterface:VenueInterface
    @Mock
    private lateinit var venueSearchListener:VenueSearchListener
    @Mock
    private lateinit var venueCheckInListener:VenueCheckInListener

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        dataManager = TestDataManager.getInstance(tokenStore, retrofit, authenticatedRetrofit)
        `when`(retrofit.create(VenueInterface::class.java)).thenReturn(venueInterface)
        `when`(authenticatedRetrofit.create(VenueInterface::class.java)).thenReturn(venueInterface)
        dataManager.addVenueSearchListener(venueSearchListener)
        dataManager.addVenueCheckInListener(venueCheckInListener)
    }

    @After
    fun tearDown(){
        reset(retrofit, authenticatedRetrofit, venueInterface, venueSearchListener,tokenStore)
        dataManager.removeVenueSearchListener(venueSearchListener)
        dataManager.removeVenueCheckInListener(venueCheckInListener)
        TestDataManager.reset()
    }

    @Test
    fun searchListenerTriggeredOnSuccessfulSearch(){
        val responseCall: Call<VenueSearchResponse> = mock()
        `when`(venueInterface.venueSearch
        (ArgumentMatchers.anyString())).thenReturn(responseCall)
        dataManager.fetchVenueSearch()
        verify(responseCall).enqueue(searchCaptor.capture())
        val venueSearchResponse:VenueSearchResponse = mock()
        val response = Response.success(venueSearchResponse)
        searchCaptor.value.onResponse(responseCall, response)
        verify(venueSearchListener).onVenueSearchFinished()
    }

    @Test
    fun venueSearchListSavedOnSuccessfulSearch(){
        val responseCall: Call<VenueSearchResponse> = mock()
        `when`(venueInterface.venueSearch(ArgumentMatchers.anyString()))
                .thenReturn(responseCall)
        dataManager.fetchVenueSearch()
        verify(responseCall).enqueue(searchCaptor.capture())
        val firstVenueName =  "Cool first venue"
        val firstVenue:Venue =  mock()
        `when`(firstVenue.name).thenReturn(firstVenueName)
        val secondVenueName = "awesome second venue"
        val secondVenue:Venue = mock()
        `when`(secondVenue.name).thenReturn(secondVenueName)
        val venueList = listOf(firstVenue, secondVenue)
        val venueSearchResponse:VenueSearchResponse = mock()
        `when`(venueSearchResponse.venues).thenReturn(venueList)
        val respone = Response.success(venueSearchResponse)
        searchCaptor.value.onResponse(responseCall, respone)
        val dataManagerVenueList = dataManager.venueList
        MatcherAssert.assertThat(dataManagerVenueList, `is`(CoreMatchers.equalTo(venueList)))

    }

    @Test
    fun checkInListenerTriggeredOnSuccessfulCheckIn(){
        val successObservable = Observable.just(Any())
        `when`(venueInterface.venueCheckIn(ArgumentMatchers.anyString())).
        thenReturn(successObservable)
        val fakeVenueId = "fakeVenueId"
        dataManager.checkInToVenue(fakeVenueId)
        verify(venueCheckInListener).onVenueCheckInFinished()
    }

    @Test
    fun checkInListenerNotifiesTokenExpiredOnUnauthorizedException(){
        val unauthorizedObservable = Observable.error<Any>(UnauthorizedException())
        `when`(venueInterface.venueCheckIn(ArgumentMatchers.anyString())).thenReturn(unauthorizedObservable)
        val fakeVenueId = "fakeVenueId"
        dataManager.checkInToVenue(fakeVenueId)
        verify(venueCheckInListener).onTokenExpired()
    }
}