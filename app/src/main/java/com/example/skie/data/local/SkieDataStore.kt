package com.example.skie.data.local
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//save and read last city to show on app start

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "skie_preference")

class SkieDataStore(private val context: Context) {

    companion object {
        val LAST_CITY_QUERY = stringPreferencesKey("last_city_query")

        //saved cities was showing coordinates instead of city names. so this was created
        val LAST_CITY_NAME = stringPreferencesKey("last_city_name")
    }

    //the last city query
    val lastCityQuery: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LAST_CITY_QUERY] ?: "auto:ip"  //if none show auto:ip
    }

    //update this "list"
    suspend fun saveLastCity(
        query: String,
    ) {
        context.dataStore.edit { preferences ->
            preferences[LAST_CITY_QUERY] = query
        }
    }
}