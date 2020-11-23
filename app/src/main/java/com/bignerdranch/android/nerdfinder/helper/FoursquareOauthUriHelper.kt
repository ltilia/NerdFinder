package com.bignerdranch.android.nerdfinder.helper

import android.net.Uri

object FoursquareOauthUriHelper {
    private const val ACCESS_TOKEN_PARAM = "access_token="

    fun getAccessToken(uri: String): String? {
        val oauthUri = Uri.parse(uri)
        val uriFragment = oauthUri.fragment

        return if (uriFragment?.contains(ACCESS_TOKEN_PARAM) == true) {
            uriFragment.substring(ACCESS_TOKEN_PARAM.length)
        } else {
            null
        }
    }
}
