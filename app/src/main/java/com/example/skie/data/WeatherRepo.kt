package com.example.skie.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.ui.geometry.Rect
import com.example.skie.data.local.CachedWeather
import com.example.skie.data.local.WeatherDatabase
import com.example.skie.data.model.DailyData
import com.example.skie.data.model.ForecastDayResponse
import com.example.skie.data.model.HourResponse
import com.example.skie.data.model.HourlyData
import com.example.skie.data.model.WeatherData
import com.example.skie.data.model.WeatherResponse
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.skie.BuildConfig


/**separted from viewmodel so it can handle, if online
 * use api or fall back on cached weather
 * converting raw JSON to clean data
 * feeding that clean data to viewmodel to handle ui state and user actions
 */
class WeatherRepo(private val context: Context) {
    //retro talk to api
    private val api: WeatherApiService = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create()) //gson to turn json to kotlin object aka the response object
        .build()
        .create(WeatherApiService::class.java)

    //reference to dao
    private val dao = WeatherDatabase.getDatabase(context).weatherDao()

    //turn api response into string for one line in db
    private val gson = Gson()

    //fun the ui will call to get the weather
    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    suspend fun getWeather(location: String): Result<WeatherData>{
        android.util.Log.d("SKIE_API", "Key: ${BuildConfig.WEATHER_API_KEY}")
        return if (isOnline()){
            try{
                //get fresh data from api
                val response = api.getForecast(
                    apiKey = BuildConfig.WEATHER_API_KEY,
                    location = location,
                    days = 6
                )

                //if online save new data into local cache
                dao.cacheWeather(
                    CachedWeather(
                        location = location,
                        weatherJSON = gson.toJson(response) //back to JSON
                    )
                )

                //return success result mapped to weather data
                Result.success(response.toWeatherData())
            } catch (e: Exception){
                //if api call fails
                loadFromCache(location) ?: Result.failure(e)
            }
        } else{
            //no internet
            loadFromCache(location) ?: Result.failure(Exception("No internet connection"))
        }
    }

    //look into the db for cached data
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadFromCache(location: String): Result<WeatherData>?{
        val cachedWeather = dao.getCachedWeather(location) ?: return null //incase no cache
        return try{
            //get the cached JSON and turn back to object
            val response = gson.fromJson(cachedWeather.weatherJSON, WeatherResponse::class.java)
            Result.success(response.toWeatherData()) //weather response to weather data
        } catch(e: Exception){null}
    }

    //utility function to check if device is online
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @SuppressLint("ServiceCast")
    private fun isOnline(): Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

//the mapper turning messy response data into clean data
@RequiresApi(Build.VERSION_CODES.O)
fun WeatherResponse.toWeatherData(): WeatherData{
    val today = forecast.forecastDay.first()
    val tomorrow = forecast.forecastDay.getOrNull(1)

    val now = java.time.LocalDateTime.now()
    val allHours = buildList {
        addAll(today.hour)
        tomorrow?.let{addAll(it.hour)}
    }
    //show the next 12 hours even if it enters the next day
    val upcomingHours = allHours.filter { hour ->
        val hourTime = java.time.LocalDateTime.parse(
            hour.time,
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        )
        hourTime.isAfter(now.minusHours(1))
    }.take(12)

    //skip today
    val forecastDays = forecast.forecastDay.drop(1)

    return WeatherData(
        location = location.name,
        country = location.country,
        tempC = current.tempC,
        feelsLikeC = current.feelsLikeC,
        highTempC = today.day.maxTempC,
        lowTempC = today.day.minTempC,
        condition = current.condition.text,
        conditionCode = current.condition.code,
        isDay = current.isDay,
        humidity = current.humidity,
        wind = current.windKph,

        //the hour here is the api's
        hourly = upcomingHours.map{ hour: HourResponse ->
            HourlyData(
                time = hour.time,
                tempC = hour.tempC,
                condition = hour.condition.text,
                conditionCode = hour.condition.code,
                iDay = hour.isDay
            )
        },

        forecast = forecastDays.map{ day: ForecastDayResponse ->
            DailyData(
                date = day.date,
                highTempC = day.day.maxTempC,
                lowTempC = day.day.minTempC,
                condition = day.day.condition.text,
                conditionCode = day.day.condition.code,
                chanceOfRain = day.day.chanceOfRain
            )
        }
    )
}

