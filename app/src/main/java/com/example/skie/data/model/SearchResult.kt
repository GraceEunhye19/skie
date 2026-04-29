package com.example.skie.data.model

import com.google.gson.annotations.SerializedName
data class SearchResult(
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String,
    @SerializedName("country") val country: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double
)
