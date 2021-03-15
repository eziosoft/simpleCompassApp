package com.eziosoft.simplecompassnetguru.repository

import android.location.Location
import androidx.lifecycle.LiveData

interface Repository {

    fun currentLocation(): LiveData<Location>
    fun currentTargetLocation():LiveData<Location>
    fun currentHeading(): LiveData<Float>
    fun currentBearing(): LiveData<Float>

    fun currentDistance(): LiveData<Float>
    fun setTargetLocation(targetLocation: Location)
    fun start()
    fun stop()


}