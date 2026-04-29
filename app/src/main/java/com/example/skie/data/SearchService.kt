package com.example.skie.data


import com.example.skie.data.model.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query
//api call for city search
interface SearchService {
    @GET("search.json")
    suspend fun searchCities(
        @Query("key") apiKey: String,
        @Query("q") query: String
    ): List<SearchResult>
}