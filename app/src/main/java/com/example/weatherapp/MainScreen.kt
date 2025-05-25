package com.example.weatherapp

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherapp.api.LocationClient


@Composable

fun MainScreen(
    navController: NavController,
    viewModel: WeatherViewModel,
    applicationContext: Context
){
    Scaffold(modifier = Modifier.fillMaxSize()
        , bottomBar = {
            BottomAppBar(modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.primary),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Row( modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { navController.navigate(Routes.settingScreen) }) { Text("Settings") }
                    Button(onClick = { navController.navigate(Routes.favoScreen) }) { Text("Favorites") }
                }
            }
        }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
            Box(modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .defaultMinSize(minWidth = 60.dp, minHeight = 60.dp)
                .border(2.dp,  MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .absoluteOffset()
                , Alignment.Center){
                WeatherUI(viewModel, applicationContext)
            }
        }
    }
}

@Composable
fun WeatherUI(
    viewModel: WeatherViewModel,
    context: Context
) {
    val weather = viewModel.weatherState
    val selectedUnit = viewModel.selectedUnit

    val locationClient = LocationClient(context)

    // State zum Anzeigen des Ladestatus
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            isLoading = true
            locationClient.getLocation { location ->
                location?.let {
                    viewModel.fetchWeather_LatLon(it.latitude, it.longitude)
                }
                isLoading = false
            }
        }) {
            Text("Wetter für aktuellen Standort abrufen")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (weather != null) {
                Text("Ort: ${weather.name}")

                val unitSymbol = when (selectedUnit) {
                    "metric" -> "°C"
                    "imperial" -> "°F"
                    else -> "K"
                }

                Text("Temperatur: ${weather.main.temp}$unitSymbol ")
                Text("Humidity: ${weather.main.humidity}%")
                Text("Wind: ${weather.wind.speed}")
                Text(weather.weather.first().description)
            } else {
                Text("Noch keine Wetterdaten verfügbar.")
            }
        }
    }
}