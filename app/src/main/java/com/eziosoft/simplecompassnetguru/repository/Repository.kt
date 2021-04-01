/*
 * Created by Bartosz Szczygiel on 3/15/21 2:18 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/15/21 2:17 PM
 */

package com.eziosoft.simplecompassnetguru.repository

import android.location.Location
import androidx.lifecycle.Lifecycle
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

    fun addLifeCycle(lifecycle: Lifecycle)


}