/*
 * Created by Bartosz Szczygiel on 3/15/21 2:18 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/15/21 2:17 PM
 */

package com.eziosoft.simplecompassnetguru.repository

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.eziosoft.simplecompassnetguru.repository.data.DeviceAttitudeProvider
import com.eziosoft.simplecompassnetguru.repository.data.LocationProvider
import com.eziosoft.simplecompassnetguru.utils.TargetCalculations
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val locationProvider: LocationProvider,
    private val deviceAttitudeProvider: DeviceAttitudeProvider
) : Repository {


    private val currentDistance = MutableLiveData<Float>()
    private val targetLocation = MutableLiveData<Location>()
    private val currentBearing = MutableLiveData<Float>()
    private val currentLocation = MutableLiveData<Location>()


    init {
        CoroutineScope(Dispatchers.IO).launch {
            locationProvider.currentLocation.collect() { location ->
                Log.d("aaa", "currentLocation: ${location}")
                if (targetLocation.value != null) {
                    currentDistance.postValue(
                        TargetCalculations.calculateDistance(
                            location,
                            targetLocation.value!!
                        )
                    )
                }
                currentLocation.postValue(location)
            }
        }
    }

    override fun currentLocation(): LiveData<Location> = currentLocation

    override fun currentHeading(): LiveData<Float> =
        deviceAttitudeProvider.attitude.map { attitude ->
//            Log.d("aaa", "currentHeading: ${currentLocation().value != null}  ${targetLocation.value != null} ")
            if (currentLocation.value != null && targetLocation.value != null) {
                currentBearing.postValue(
                    TargetCalculations.calculateBearing(
                        currentLocation.value!!,
                        targetLocation.value!!
                    )
                )
            }
            attitude.azimuth.toFloat()
        }

    override fun currentTargetLocation(): LiveData<Location> = targetLocation
    override fun setTargetLocation(targetLocation: Location) {
        this.targetLocation.postValue(targetLocation)
    }

    override fun currentBearing(): LiveData<Float> = currentBearing
    override fun currentDistance(): LiveData<Float> = currentDistance

    override fun addLifeCycle(lifecycle: Lifecycle) {
        deviceAttitudeProvider.addLifeCycle(lifecycle)
        locationProvider.addLifeCycle(lifecycle)
    }


}