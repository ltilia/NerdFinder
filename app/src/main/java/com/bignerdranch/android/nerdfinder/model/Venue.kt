package com.bignerdranch.android.nerdfinder.model

import com.google.gson.annotations.SerializedName

class Venue {
    val id: String = ""
    val name: String = ""
    private val verified = false
    private val location: Location? = null
    @SerializedName("categories")
    private val categoryList = emptyList<Category>()

    val formattedAddress: String
        get() = location?.formattedAddress ?: ""
}
