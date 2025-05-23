package com.example.weatherapp.persistance

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CachedWeather::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}