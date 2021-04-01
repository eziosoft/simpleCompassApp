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
import com.eziosoft.simplecompassnetguru.utils.TAG
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@Singleton
class LocationProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {
    val currentLocation = locationFlow()


    private fun locationFlow() = callbackFlow<Location> {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    sendBlocking(location)
                }
            }
        }

        var permissionGranted = true
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "setupFusedLocationProvider: NOT GRANTED")
            permissionGranted = false
        }

        if (permissionGranted) {
            val locationRequest = LocationRequest.create()?.apply {
                interval = 1000
                fastestInterval = 1000 / 2
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            Log.d(TAG, "start: LocationProvider")
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

        awaitClose {
            Log.d(TAG, "stop: LocationProvider")
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }


}