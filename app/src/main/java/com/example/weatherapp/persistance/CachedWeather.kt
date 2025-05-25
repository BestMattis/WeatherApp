package com.example.weatherapp.persistance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class CachedWeather(
    @PrimaryKey val id : String = "",
    val latitude : Double,
    val longitude: Double,
    val temperature : Float,
    val humidity : Int,
    val description : String,
    val windSpeed : Float,
    val timestamp : Long,
    val favTag : Boolean,
    val unit : String,
    val iconId : String
)