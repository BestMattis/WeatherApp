package com.example.weatherapp.api

data class WeatherResponse(
    val name : String,
    val main : Main,
    val coord : Coord,
    val wind : Wind,
    val weather : List<Weather>
)

data class Coord(
    val lat : Double,
    val lon : Double
)

data class Weather(
    val description : String,
    val icon : String
)

data class Main(
    val temp : Float,
    val humidity : Int
)

data class Wind(
    val speed : Float
)
