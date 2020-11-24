package com.bignerdranch.android.nerdfinder.web

import com.bignerdranch.android.nerdfinder.model.TokenStore
import com.bignerdranch.android.nerdfinder.model.VenueSearchResponse
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.*
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

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }
}