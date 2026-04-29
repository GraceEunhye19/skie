package com.example.skie.data

import retrofit2.http.Query
import com.example.skie.data.model.WeatherResponse
import retrofit2.http.GET

//network interface serving a contract to tell retrofit how to talk to the api server
//an order to the server. a menu in the kitchen
interface WeatherApiService {
    @GET("forecast.json") //retrofit should get the data
    //run in the background
    suspend fun getForecast(
        @Query("key") apiKey: String, //adds my key for access
        @Query("q") location: String, //q is the standard name for a search term. this is for the search feature
        @Query("days") days: Int = 6, //5 days minus the first day
        @Query("aqi") aqi: String = "no", //no air quality index data
        @Query("alerts") alerts: String = "no" //no weather alerts
    ): WeatherResponse
}