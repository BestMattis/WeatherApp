package com.example.weatherapp.api

data class WeatherResponse(
    val name : String,
    val main : Main,
    val wind : Wind,
    val weather : List<Weather>
)

data class Weather(
    val description : String
)

data class Main(
    val temp : Float,
    val humidity : Int
)

data class Wind(
    val speed : Float
)
