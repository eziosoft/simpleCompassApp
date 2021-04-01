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
import com.eziosoft.simplecompassnetguru.repository.data.Attitude
import com.eziosoft.simplecompassnetguru.repository.data.DeviceAttitudeProvider
import com.eziosoft.simplecompassnetguru.repository.data.LocationProvider
import com.eziosoft.simplecompassnetguru.utils.TAG
import com.eziosoft.simplecompassnetguru.utils.TargetCalculations
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class DefaultRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val locationProvider: LocationProvider,
    private val deviceAttitudeProvider: DeviceAttitudeProvider
) : Repository, LifecycleObserver {


    private val currentDistance = MutableLiveData<Float>()
    private val targetLocation = MutableLiveData<Location>()
    private val currentBearing = MutableLiveData<Float>()
    private val currentHeading = MutableLiveData<Float>()
    private val currentLocation = MutableLiveData<Location>()

    private var job: Job? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun start() {
        Log.i(TAG, "start: Repository")
        job = CoroutineScope(Dispatchers.IO).launch {
            launch {
                locationProvider.currentLocation.collect() { location ->
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

            launch {
                deviceAttitudeProvider.attitude.collect() { attitude ->
                    if (currentLocation.value != null && targetLocation.value != null) {
                        currentBearing.postValue(
                            TargetCalculations.calculateBearing(
                                currentLocation.value!!,
                                targetLocation.value!!
                            )
                        )
                    }

                    currentHeading.postValue(attitude.azimuth.toFloat())
                }
            }

        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun stop() {
        Log.i(TAG, "stop: Repository")
        job?.cancel()
    }

    override fun currentLocation():LiveData<Location> = currentLocation
    override fun currentHeading():LiveData<Float> = currentHeading

    override fun currentTargetLocation() = targetLocation
    override fun setTargetLocation(targetLocation: Location) {
        this.targetLocation.postValue(targetLocation)
    }

    override fun currentBearing(): LiveData<Float> = currentBearing
    override fun currentDistance(): LiveData<Float> = currentDistance

    override fun addLifeCycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }


}