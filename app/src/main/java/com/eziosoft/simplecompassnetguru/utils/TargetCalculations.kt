/*
 * Created by Bartosz Szczygiel on 3/15/21 2:18 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/15/21 2:17 PM
 */

package com.eziosoft.simplecompassnetguru.utils

import android.location.Location

object TargetCalculations {
    fun calculateBearing(currentLocation: Location, targetLocation: Location): Float =
        currentLocation.bearingTo(targetLocation)

    fun calculateDistance(currentLocation: Location, targetLocation: Location): Float =
        currentLocation.distanceTo(targetLocation)

}