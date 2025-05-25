package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room.databaseBuilder
import com.example.weatherapp.persistance.WeatherDatabase
import com.example.weatherapp.ui.theme.WeatherAppViewModelTestTheme

class MainActivity : ComponentActivity() {

    companion object{
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "metric")
    }

    private lateinit var viewModel : WeatherViewModel
    private val apiKey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ), 0)

        val db = WeatherDatabase.getDatabase(applicationContext)
        viewModel = WeatherViewModel(dataStore, apiKey, db.weatherDao())

        setContent {
            WeatherAppViewModelTestTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.mainScreen, builder = {
                    composable(Routes.mainScreen) {
                        MainScreen(navController, viewModel, applicationContext)
                    }

                    composable(Routes.settingScreen) {
                        SettingScreen(navController, viewModel)
                    }

                    composable(Routes.favoScreen) {
                        FavoScreen(navController, viewModel)
                    }
                })
            }
        }
    }
}