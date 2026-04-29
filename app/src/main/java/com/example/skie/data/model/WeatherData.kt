package com.example.skie.data.model

//clean data used by ui
data class WeatherData(
    val location: String,
    val country: String,
    val tempC: Double, //celsius
    val feelsLikeC: Double, //also in celsius
    val highTempC: Double,
    val lowTempC: Double,
    val condition: String, //description of the condition
    val conditionCode: Int, //from the api to pick the allocated animated icon
    val isDay: Int, //1 for day, 0 for night. could be bool but int is easier for json
    val humidity: Int, //%
    val wind: Double, //speed
    val hourly: List<HourlyData>, //list of weather for the next 24 hours
    val forecast: List<DailyData>, //list for upcoming days
)

//data class for a specific hour
data class HourlyData(
    val time: String,
    val tempC: Double,
    val condition: String, //description for the hour
    val conditionCode: Int, //code for icon
    val iDay: Int, //for moon or sun
)

//data class for each day
data class DailyData(
    val date: String,  //calendar date
    val highTempC: Double,
    val lowTempC: Double,
    val condition: String,
    val conditionCode: Int,
    val chanceOfRain: Int, //%
)
