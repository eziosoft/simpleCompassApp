package com.eziosoft.simplecompassnetguru.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eziosoft.simplecompassnetguru.utils.Orientation
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    @ApplicationContext val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : RepositoryInterface {

    private val currentHeading = MutableLiveData<Float>()
    private val currentLocation = MutableLiveData<Location>()
    private val currentBearing = MutableLiveData<Float>()
    private val currentDistance = MutableLiveData<Float>()
    private val targetLocation = MutableLiveData<Location>()

    private val orientation by lazy {
        Orientation(
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager,
            object : Orientation.OrientationListener {
                override fun onSensorChanged(azimuth: Double, pitch: Double, roll: Double) {
                    currentHeading.postValue(azimuth.toFloat())
                    if (currentLocation.value != null && targetLocation.value != null)
                        currentBearing.postValue(
                            calculateBearing(
                                currentLocation.value!!,
                                targetLocation.value!!
                            )
                        )
                }
            })
    }


    override fun currentLocation(): LiveData<Location> = currentLocation
    override fun currentHeading(): LiveData<Float> = currentHeading
    override fun currentBearing(): LiveData<Float> = currentBearing
    override fun currentDistance(): LiveData<Float> = currentDistance

    override fun setTargetLocation(location: Location) {
        targetLocation.postValue(location)
    }

    override fun start() {
        orientation.start(false)
        setupFusedLocationProvider()
    }

    override fun stop() {
        orientation.stop()
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
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        Log.d("aaa", "setupFusedLocationProvider: started")
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                currentLocation.postValue(location)
                if (currentLocation.value != null && targetLocation.value != null)
                    currentDistance.postValue(
                        calculateDistance(
                            currentLocation.value!!,
                            targetLocation.value!!
                        )
                    )

                Log.d("aaa", "onLocationResult: ${location.toString()}")
            }
        }
    }


    private fun calculateBearing(currentLocation: Location, targetLocation: Location): Float =
        currentLocation.bearingTo(targetLocation)

    private fun calculateDistance(currentLocation: Location, targetLocation: Location): Float =
        currentLocation.distanceTo(targetLocation)

}