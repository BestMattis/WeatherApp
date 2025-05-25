package com.example.weatherapp

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.weatherapp.api.WeatherResponse

@Composable
fun FavoScreen(
    navController: NavController,
    viewModel: WeatherViewModel,
){

    var favoList = viewModel.favoList
    var selectedUnit = viewModel.selectedUnit

    val showNameDialog = remember { mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        favoList.forEach { item ->
            favoItem(item, selectedUnit, viewModel)
        }
        if(showNameDialog.value){
            CityNameDialog(onDismissRequest = {}, showNameDialog, viewModel)
        }
        Button(onClick = {
            showNameDialog.value = true
        }) { Text("Add New") }

    }
}

@Composable
fun favoItem(weatherData : WeatherResponse, unit : String, viewModel: WeatherViewModel){
    Column(Modifier
        .fillMaxWidth()
        .border(
            2.dp,
            MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
        ), horizontalAlignment = Alignment.CenterHorizontally){
        Text("Ort: ${weatherData.name}")
        Row(){

            val unitSymbol = when (unit) {
                "metric" -> "°C"
                "imperial" -> "°F"
                else -> "K"
            }

            Text("Temp: ${weatherData.main.temp}$unitSymbol ")
            Text("Humid: ${weatherData.main.humidity}% ")
            Text("Wind: ${weatherData.wind.speed} ")
        }
        Row(){
            Text(weatherData.weather.first().description)
            Button(onClick = {
                viewModel.removeFavo(weatherData)
            }) {
                Text("X")
            }
        }

    }
}

@Composable
fun CityNameDialog(
    onDismissRequest: () -> Unit,
    showNameDialog: MutableState<Boolean>,
    viewModel: WeatherViewModel
) {
    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            var text = remember { mutableStateOf("") }

            TextField(
                value = text.value,
                onValueChange = { text.value = it },
                label = { Text("Enter City Name") },
                //placeholder = { Text("London") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Button(onClick = {
                viewModel.fetchWeather_Name(text.value)
                showNameDialog.value = false
                onDismissRequest()
            }){
                Text("Submit")
            }
        }
    }
}