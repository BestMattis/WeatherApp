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
import com.example.weatherapp.api.RetrofitClient
import com.example.weatherapp.api.WeatherResponse
import com.example.weatherapp.persistance.PreferenceKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val dataStore : DataStore<Preferences>,
    private val apiKey : String
) : ViewModel() {

    var weatherState by mutableStateOf<WeatherResponse?>(null)
        private set

    val favoList = mutableStateListOf<WeatherResponse>()//by mutableListOf<List<WeatherResponse>?>(null)

    var selectedUnit by mutableStateOf("metric") // default Celsius
        private set

    init {
        readUnit()
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
            }
        }
    }

    fun setUnit(unit: String) {
        selectedUnit = unit
        saveUnit(unit)
    }

    fun addToFavoList(weatherData : WeatherResponse){
        favoList.add(weatherData)
    }

    fun fetchWeather_LatLon(lat : Double, lon : Double) {
        viewModelScope.launch {
            try {
                val weather = RetrofitClient.api.getCurrentWeather_LatLon(lat, lon, apiKey, selectedUnit)
                weatherState = weather
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchWeather_Name(cityName : String) {
        viewModelScope.launch {
            try {
                val weather = RetrofitClient.api.getCurrentWeather_Name(cityName, apiKey, selectedUnit)
                println(weather)
                addToFavoList(weather)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}