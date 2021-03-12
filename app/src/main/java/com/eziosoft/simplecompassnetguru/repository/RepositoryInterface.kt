package com.eziosoft.simplecompassnetguru.repository

import android.location.Location
import androidx.lifecycle.LiveData

interface RepositoryInterface {

    fun currentLocation(): LiveData<Location>
    fun currentHeading(): LiveData<Float>
    fun currentBearing(): LiveData<Float>
    fun currentDistance(): LiveData<Float>
//    fun targetLocation(): LiveData<Location>

    fun setTargetLocation(targetLocation: Location)


    fun start()
    fun stop()


}