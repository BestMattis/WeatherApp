package com.example.weatherapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Coord
import com.example.weatherapp.api.Main
import com.example.weatherapp.api.RetrofitClient
import com.example.weatherapp.api.Weather
import com.example.weatherapp.api.WeatherResponse
import com.example.weatherapp.api.Wind
import com.example.weatherapp.persistance.CachedWeather
import com.example.weatherapp.persistance.PreferenceKeys
import com.example.weatherapp.persistance.WeatherDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class WeatherViewModel(
    private val dataStore: DataStore<Preferences>,
    private val apiKey: String,
    private val weatherDao: WeatherDao,

    ) : ViewModel() {

    var weatherState by mutableStateOf<WeatherResponse?>(null)
        private set

    val favoList = mutableStateListOf<WeatherResponse>()//by mutableListOf<List<WeatherResponse>?>(null)

    var selectedUnit by mutableStateOf("metric") // default Celsius
        private set

    init {
        readUnit()
        readFavorites()
    }

    fun saveUnit(unit : String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[PreferenceKeys.UNIT] = unit
            }
        }
    }

    fun removeFavo(weatherData : WeatherResponse){
        favoList.remove(weatherData)
    }

    fun readUnit() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { preferences ->
                selectedUnit = preferences[PreferenceKeys.UNIT].toString()

                if (selectedUnit == "null"){
                    selectedUnit = "metric"
                }
            }
        }
    }

    fun readFavorites(){
        viewModelScope.launch {
           // favoList = weatherDao.getFavorites()
        }

    }

    fun setUnit(unit: String) {
        selectedUnit = unit
        saveUnit(unit)
    }

    fun addToFavoList(weatherData : WeatherResponse){
        favoList.add(weatherData)
    }

    fun fetchWeather_LatLon(latVal : Double, lonVal : Double) {

        val lat = ((latVal * 1000).toInt()).toDouble() / 1000
        val lon = ((lonVal * 1000).toInt()).toDouble() / 1000

        viewModelScope.launch {
            try {
                val cacheData = weatherDao.getWeather(lat = lat, lon = lon)
                if (cacheData == null || System.currentTimeMillis() - cacheData.timestamp > 600000){

                    println("new Data with units: $selectedUnit")

                    val weather = RetrofitClient.api.getCurrentWeather_LatLon(lat, lon, apiKey, selectedUnit)
                    println(weather)
                    weatherState = weather
                    weatherDao.insertWeather(CachedWeather(
                        id = weather.name.trim(' ').lowercase(),
                        temperature = weather.main.temp,
                        humidity = weather.main.humidity,
                        description = weather.weather.first().description,
                        windSpeed = weather.wind.speed,
                        timestamp = System.currentTimeMillis(),
                        longitude = lon,
                        latitude = lat,
                        favTag = false
                    ))
                } else {

                    weatherState = WeatherResponse(
                        name = cacheData.id,
                        main = Main(temp = cacheData.temperature, humidity = cacheData.humidity),
                        wind = Wind(cacheData.windSpeed),
                        coord = Coord(lat = cacheData.latitude, lon = cacheData.longitude),
                        weather = listOf(Weather(
                            description = cacheData.description
                        ))
                    )
                    println("cached $weatherState")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchWeather_Name(cityName : String) {

        val sanatizedName = cityName.trim(' ').lowercase()

        println(sanatizedName)

        viewModelScope.launch {
            try {
                val cacheData = weatherDao.getWeather(name = sanatizedName)
                if (cacheData == null || System.currentTimeMillis() - cacheData.timestamp > 600000){

                    println("new Data")

                    val weather = RetrofitClient.api.getCurrentWeather_Name(sanatizedName, apiKey, selectedUnit)
                    val cachedWeather = CachedWeather(
                        id = sanatizedName,
                        temperature = weather.main.temp,
                        humidity = weather.main.humidity,
                        description = weather.weather.first().description,
                        windSpeed = weather.wind.speed,
                        timestamp = System.currentTimeMillis(),
                        longitude = ((weather.coord.lat * 1000).toInt()).toDouble() / 1000,
                        latitude = ((weather.coord.lon * 1000).toInt()).toDouble() / 1000,
                        favTag = true
                    )
                    weatherDao.insertWeather(cachedWeather)

                    addToFavoList(weather)
                } else {

                    addToFavoList(WeatherResponse(
                        name = cacheData.id,
                        main = Main(temp = cacheData.temperature, humidity = cacheData.humidity),
                        wind = Wind(cacheData.windSpeed),
                        coord = Coord(lat = cacheData.latitude, lon = cacheData.longitude),
                        weather = listOf(Weather(
                            description = cacheData.description
                        ))
                    ))
                    println("cached")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}