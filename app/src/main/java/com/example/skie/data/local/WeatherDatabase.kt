package com.example.skie.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [CachedWeather::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object{
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "skie_weather_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}