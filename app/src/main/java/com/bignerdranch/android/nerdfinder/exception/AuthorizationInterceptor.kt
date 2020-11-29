package com.bignerdranch.android.nerdfinder.exception

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection

class AuthorizationInterceptor:Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if(response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
            throw  UnauthorizedException()
        }

        return  response
    }
}