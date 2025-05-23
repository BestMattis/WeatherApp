package com.example.weatherapp.persistance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class CachedWeather(
    @PrimaryKey val id: Int = 0,
    val temperature: Double,
    val description: String,
    val timestamp: Long
)