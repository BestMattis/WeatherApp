package com.example.weatherapp.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherapp.WeatherViewModel

// base structure from https://developer.android.com/develop/ui/compose/components/radio-button?hl=de
@Composable
fun SettingScreen(navController: NavController, viewModel: WeatherViewModel) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val radioOptions = listOf(
            "metric" to "Celsius (°C)",
            "imperial" to "Fahrenheit (°F)",
            "standard" to "Kelvin (K)"
        )

        var currentSetting = "Celsius (°C)"
        radioOptions.forEach { (value, lable) -> if (value == viewModel.selectedUnit) currentSetting = lable }

        val (selectedOption, onOptionSelected) = remember { mutableStateOf(currentSetting) }

        Column(Modifier.selectableGroup()) {
            radioOptions.forEach { (value, label) ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (label == selectedOption),
                            onClick = { onOptionSelected(label)
                                        viewModel.setUnit(value)
                                      },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (label == selectedOption),
                        onClick = null
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}