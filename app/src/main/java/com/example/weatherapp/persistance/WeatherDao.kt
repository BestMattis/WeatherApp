package com.example.weatherapp.persistance

import androidx.room.*

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather LIMIT 1")
    suspend fun getWeather(): CachedWeather?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: CachedWeather)
}