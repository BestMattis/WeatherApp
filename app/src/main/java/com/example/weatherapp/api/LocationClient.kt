package com.example.weatherapp.api

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.activity.ComponentActivity
import com.example.weatherapp.hasLocationPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener


class LocationClient(val context : Context) : ComponentActivity() {

    private lateinit var client : FusedLocationProviderClient

    @SuppressLint("MissingPermission")

    fun getLocation(callback: (Location?) -> Unit){
        // checking Permission
        if (!context.hasLocationPermission()) {
            throw Exception("Missing location permission")
        }

        val locationManager =
            context.getSystemService(LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGpsEnabled && !isNetworkEnabled) {
            throw Exception("GPS is disabled")
        }

        // get current Location
        client = LocationServices.getFusedLocationProviderClient(context)

        client.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    println("latitude: $lat , longitude: $lon")

                    callback(location)
                }
            }
    }
}