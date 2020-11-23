package com.bignerdranch.android.nerdfinder.web

import com.bignerdranch.android.nerdfinder.model.VenueSearchResponse
import com.google.gson.*
import java.lang.reflect.Type

class VenueListDeserializer : JsonDeserializer<VenueSearchResponse?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        type: Type,
        context: JsonDeserializationContext
    ): VenueSearchResponse? {
        val responseElement: JsonElement = json.asJsonObject.get("response")
        return Gson().fromJson(responseElement, VenueSearchResponse::class.java)
    }
}
