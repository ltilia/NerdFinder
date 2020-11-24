package com.bignerdranch.android.nerdfinder.web

import com.bignerdranch.android.nerdfinder.listener.VenueSearchListener
import com.bignerdranch.android.nerdfinder.model.TokenStore
import com.bignerdranch.android.nerdfinder.model.VenueSearchResponse
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.*
import org.mockito.Mockito.`when`
import org.mockito.Mockito.reset
import retrofit2.Callback
import retrofit2.Retrofit

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

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        dataManager = TestDataManager.getInstance(tokenStore, retrofit, authenticatedRetrofit)
        `when`(retrofit.create(VenueInterface::class.java)).thenReturn(venueInterface)
        dataManager.addVenueSearchListener(venueSearchListener)
    }

    @After
    fun tearDown(){
        reset(retrofit, authenticatedRetrofit, venueInterface, venueSearchListener,tokenStore)
        dataManager.removeVenueSearchListener(venueSearchListener)
        TestDataManager.reset()
    }
}