package com.example.weatherapp.persistance

import androidx.room.*

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE id LIKE :name OR longitude LIKE :lon AND latitude Like :lat LIMIT 1")
    suspend fun getWeather(name : String = "", lat : Double = 0.0, lon : Double = 0.0): CachedWeather?

    @Query("SELECT * FROM weather WHERE favTag LIKE :favTagVar")
    suspend fun getFavorites(favTagVar : Boolean = true): CachedWeather?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: CachedWeather)
}