package com.example.skie.ui

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.skie.data.WeatherRepo
import com.example.skie.data.local.SkieDataStore
import com.example.skie.data.model.WeatherData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//placed outside for scalability and skeleton loader
sealed class WeatherUiState{
    object Loading: WeatherUiState() //loading screen
    data class Success(val data: WeatherData): WeatherUiState() //what we want
    data class Error(val message: String): WeatherUiState() //what we dont
    object Offline: WeatherUiState() //no internet
}

class WeatherViewModel(application: Application): AndroidViewModel(application){

    private val repo = WeatherRepo(application)

    private val dataStore = SkieDataStore(application)

    //gps antenna. a location api from googles play services
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading) //private mutable ui state that only viewmodel can change
    val uiState = _uiState //public ui state that can be observed by the ui

    //track the city being viewed for easy refresh
    private val _currentLocation = MutableStateFlow("auto:ip") //can be kaduna if issues arise

    //load last city
    init {
        loadLastCity()
    }

    private fun loadLastCity(){
        viewModelScope.launch {
            val lastQuery = dataStore.lastCityQuery.first() //the last city searched
            if(lastQuery == "auto:ip"){fetchFromGps()} else{ fetchWeather(lastQuery)}
        }
    }

    // called on first launch after permission is granted
    fun fetchFromGps() {
        val context = getApplication<Application>()

        val hasFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            // no permission fall on auto
            fetchWeather("auto:ip")
            return
        }

        _uiState.value = WeatherUiState.Loading

        val cancellationToken = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationToken.token
        )
            .addOnSuccessListener { location ->
                if (location != null) {
                    val query = "${location.latitude},${location.longitude}"
                    viewModelScope.launch {
                        dataStore.saveLastCity(query)
                    }
                    fetchWeather(query)
                } else {
                    // gps returned null
                    fetchWeather("auto:ip")
                }
            }
            .addOnFailureListener {
                // gps failed
                fetchWeather("auto:ip")
            }
    }


    fun fetchWeather(location: String){
        viewModelScope.launch { //coroutine to avoid freezing
            _uiState.value = WeatherUiState.Loading //set screen to loading
            _currentLocation.value = location //update current city name
            val result = repo.getWeather(location) //ask repo for data, needs networkstate cuz of connectivity manager

            //upadte ui state based on repo's result
            _uiState.value = if(result.isSuccess){
                WeatherUiState.Success(result.getOrNull()!!) //success
            } else{
                val message = result.exceptionOrNull()?.message ?: "Something went Wrong"
                if (message.contains("No Internet")){
                    WeatherUiState.Offline
                } else {
                    WeatherUiState.Error(message)
                }

            }

        }
    }

    //save and add it to list
    fun selectCity(query: String){
        viewModelScope.launch {
            dataStore.saveLastCity(query)
            //dataStore.addSavedCity(query, displayName)
            fetchWeather(query)
        }
    }

    fun refresh(){
        viewModelScope.launch {
            val lastQuery = dataStore.lastCityQuery.first()
            fetchWeather(lastQuery)
        }
    }
}