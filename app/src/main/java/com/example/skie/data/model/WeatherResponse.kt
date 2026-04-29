package com.example.skie.data.model

import com.google.gson.annotations.SerializedName

//class to hold every thing aka raw JSON returned to the api
//raw data containing data and response
data class WeatherResponse(
    @SerializedName("location") val location: LocationResponse, //maps to location section
    @SerializedName("current") val current: CurrentResponse, //current weather section
    @SerializedName("forecast") val forecast: ForecastResponse, //daily/hourly section
)

data class LocationResponse(
    @SerializedName("name") val name: String, //city name
    @SerializedName("country") val country: String, //country name
)

data class CurrentResponse(
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("feelslike_c") val feelsLikeC: Double,
    @SerializedName("condition") val condition: ConditionResponse, //for text and icon
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("is_day") val isDay: Int, //still 0 or 1
)

data class ConditionResponse(
    @SerializedName("text") val text: String, //e.g. light rain
    @SerializedName("code") val code: Int, //code for animated icon
)

//section of JSON that contains list of future days
data class ForecastResponse(
    @SerializedName("forecastday") val forecastDay: List<ForecastDayResponse>
)
//one day in the forecast(date, day and list of hours)
data class ForecastDayResponse(
    @SerializedName("date") val date: String,
    @SerializedName("day") val day: DayResponse,
    @SerializedName("hour") val hour: List<HourResponse>
)

//data for a specific day
data class DayResponse(
    @SerializedName("maxtemp_c") val maxTempC: Double,
    @SerializedName("mintemp_c") val minTempC: Double,
    @SerializedName("daily_chance_of_rain") val chanceOfRain: Int,
    @SerializedName("condition") val condition: ConditionResponse //icon and text for the day
)

//data for a specific hour
data class HourResponse(
    @SerializedName("time") val time: String,
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("is_day") val isDay: Int,
    @SerializedName("condition") val condition: ConditionResponse
)
