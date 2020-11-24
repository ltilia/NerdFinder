package com.bignerdranch.android.nerdfinder.web

import com.bignerdranch.android.nerdfinder.model.TokenStore
import retrofit2.Retrofit

class TestDataManager private constructor(tokenStore: TokenStore,
                                          retrofit: Retrofit,
                                          authenticatedRetrofit:Retrofit):DataManager(tokenStore, retrofit,authenticatedRetrofit){

    companion object{
        fun getInstance(tokenStore: TokenStore, retrofit: Retrofit,
                        authenticatedRetrofit:Retrofit):DataManager{

            if(dataManager==null){
                dataManager = TestDataManager(tokenStore, retrofit, authenticatedRetrofit)
            }
            return dataManager!!

        }
    }
}