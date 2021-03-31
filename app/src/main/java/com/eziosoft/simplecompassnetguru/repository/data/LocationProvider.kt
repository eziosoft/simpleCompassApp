/*
 * Created by Bartosz Szczygiel on 3/31/21 9:29 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/31/21 9:29 PM
 */

package com.eziosoft.simplecompassnetguru.repository.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocationProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LifecycleObserver {

    private val _currentLocation = MutableSharedFlow<Location>()
    val currentLocation: SharedFlow<Location> = _currentLocation

    fun addLifeCycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start() {
        Log.d("aaa", "start: LocationProvider")
        setupFusedLocationProvider()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Log.d("aaa", "stop: LocationProvider")
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    private fun setupFusedLocationProvider() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("aaa", "setupFusedLocationProvider: NOT GRANTED")
            return
        }

        val locationRequest = LocationRequest.create()?.apply {
            interval = 1000
            fastestInterval = 1000 / 2
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            Log.d("aaa", "onLocationResult: ")
            locationResult ?: return
            for (location in locationResult.locations) {
                CoroutineScope(Dispatchers.IO).launch {
                    _currentLocation.emit(location)
                }
            }
        }
    }
}