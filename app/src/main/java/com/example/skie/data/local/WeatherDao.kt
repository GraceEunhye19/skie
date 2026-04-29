package com.example.skie.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {

    @Query("SELECT * FROM cached_weather WHERE location = :location LIMIT 1")
    suspend fun getCachedWeather(location: String): CachedWeather?

    @Insert(onConflict = OnConflictStrategy.REPLACE) //overide old data
    suspend fun cacheWeather(weather: CachedWeather)

    @Query("DELETE from cached_weather")
    suspend fun clearAll()
}