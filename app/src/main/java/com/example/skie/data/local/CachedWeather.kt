package com.example.skie.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

//entity/table
@Entity(tableName = "cached_weather")
data class CachedWeather(
    @PrimaryKey //unique id for each row
    val location: String,
    val weatherJSON: String, //stores the api response as JSON string
    val timeStamp: Long = System.currentTimeMillis()
)

