package com.bignerdranch.android.nerdfinder.model

import android.content.Context
import androidx.preference.PreferenceManager

class TokenStore private constructor(context: Context) {

    private val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(context.applicationContext)

    var accessToken: String?
        get() = sharedPreferences.getString(TOKEN_KEY, null)
        set(accessToken) = sharedPreferences.edit()
                .putString(TOKEN_KEY, accessToken)
                .apply()

    companion object {
        private const val TOKEN_KEY = "TokenStore.TokenKey"

        private var sTokenStore: TokenStore? = null

        fun getInstance(context: Context): TokenStore {
            if (sTokenStore == null) {
                sTokenStore = TokenStore(context)
            }
            return sTokenStore!!
        }
    }
}
