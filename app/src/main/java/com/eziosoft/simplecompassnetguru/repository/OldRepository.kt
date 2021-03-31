//
//package com.eziosoft.simplecompassnetguru.repository
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.hardware.SensorManager
//import android.location.Location
//import android.os.Looper
//import android.util.Log
//import androidx.core.app.ActivityCompat
//import androidx.lifecycle.*
//import com.eziosoft.simplecompassnetguru.utils.DeviceAttitude
//import com.eziosoft.simplecompassnetguru.utils.TargetCalculations
//import com.google.android.gms.location.*
//import dagger.hilt.android.qualifiers.ApplicationContext
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class OldRepository @Inject constructor(
//    @ApplicationContext val context: Context,
//    private val fusedLocationProviderClient: FusedLocationProviderClient
//) : Repository, LifecycleObserver {
//
//    private val currentHeading = MutableLiveData<Float>()
//    private val currentLocation = MutableLiveData<Location>()
//    private val currentBearing = MutableLiveData<Float>()
//    private val currentDistance = MutableLiveData<Float>()
//    private val targetLocation = MutableLiveData<Location>()
//
//    private val orientation by lazy {
//        DeviceAttitude(
//            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager,
//            object : DeviceAttitude.DeviceAttitudeListener {
//                override fun onSensorChanged(azimuth: Double, pitch: Double, roll: Double) {
//                    currentHeading.postValue(azimuth.toFloat())
//                    if (currentLocation.value != null && targetLocation.value != null) {
//                        currentBearing.postValue(
//                            TargetCalculations.calculateBearing(
//                                currentLocation.value!!,
//                                targetLocation.value!!
//                            )
//                        )
//                    }
//                }
//            })
//    }
//
//
//    override fun currentLocation(): LiveData<Location> = currentLocation
//    override fun currentTargetLocation(): LiveData<Location> = targetLocation
//
//    override fun currentHeading(): LiveData<Float> = currentHeading
//    override fun currentBearing(): LiveData<Float> = currentBearing
//    override fun currentDistance(): LiveData<Float> = currentDistance
//
//    override fun setTargetLocation(targetLocation: Location) {
//        this.targetLocation.postValue(targetLocation)
//    }
//
//    override fun addLifeCycle(lifecycle: Lifecycle) {
//        lifecycle.addObserver(this)
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    private fun start() {
//        orientation.start(false)
//        setupFusedLocationProvider()
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//    private fun stop() {
//        orientation.stop()
//        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
//    }
//
//
//    private fun setupFusedLocationProvider() {
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            Log.e("aaa", "setupFusedLocationProvider: NOT GRANTED")
//            return
//        }
//
//        val locationRequest = LocationRequest.create()?.apply {
//            interval = 1000
//            fastestInterval = 1000
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//        fusedLocationProviderClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            Looper.getMainLooper()
//        )
//    }
//
//
//    private val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult?) {
//            locationResult ?: return
//            for (location in locationResult.locations) {
//                currentLocation.postValue(location)
//                if (currentLocation.value != null && targetLocation.value != null)
//                    currentDistance.postValue(
//                        TargetCalculations.calculateDistance(
//                            currentLocation.value!!,
//                            targetLocation.value!!
//                        )
//                    )
//            }
//        }
//    }
//
//
//}