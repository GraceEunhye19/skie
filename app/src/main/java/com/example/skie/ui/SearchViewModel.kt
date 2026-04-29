package com.example.skie.ui


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.skie.BuildConfig
import com.example.skie.data.SearchService
import com.example.skie.data.model.SearchResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(FlowPreview::class)
class SearchViewModel (application: Application): AndroidViewModel(application){

    private val searchApi: SearchService = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchService::class.java)

    val searchQuery = MutableStateFlow("")

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {

        viewModelScope.launch {
            searchQuery
                .debounce(200)
                .filter { it.length >= 2 }
                .collectLatest { query ->
                    searchCities(query)
                }
        }
    }

    private suspend fun searchCities(query: String) {
        _isSearching.value = true
        _error.value = null
        try {
            val results = searchApi.searchCities(
                apiKey = BuildConfig.WEATHER_API_KEY,
                query = query
            )
            _searchResults.value = results
        } catch (e: Exception) {
            _error.value = "Could not find cities. Check your connection."
            _searchResults.value = emptyList()
        } finally {
            _isSearching.value = false
        }
    }

    fun clearSearch() {
        searchQuery.value = ""
        _searchResults.value = emptyList()
    }

}