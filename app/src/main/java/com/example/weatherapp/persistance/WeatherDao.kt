package com.example.weatherapp.persistance

import androidx.room.*

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE id = :name OR (longitude = :lon AND latitude = :lat) LIMIT 1")
    suspend fun getWeather(name : String = "", lat : Double = 0.0, lon : Double = 0.0): CachedWeather?

    @Query("SELECT * FROM weather WHERE favTag = :favTagVar")
    suspend fun getFavorites(favTagVar : Boolean = true): List<CachedWeather>?

    @Query("SELECT * FROM weather")
    suspend fun getAll(): List<CachedWeather>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: CachedWeather)

    @Query("UPDATE weather SET favTag = :tag WHERE id = :name")
    suspend fun updateFavoTag(tag : Boolean, name : String)
}