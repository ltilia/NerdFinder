package com.bignerdranch.android.nerdfinder.model

import com.google.gson.annotations.SerializedName

class Location {
    @SerializedName("lat")
    private val latitude = 0.0
    @SerializedName("lng")
    private val longitude = 0.0
    @SerializedName("formattedAddress")
    private val _formattedAddress = emptyList<String>()

    val formattedAddress: String
        get() = _formattedAddress.joinToString(separator = " ")
}
