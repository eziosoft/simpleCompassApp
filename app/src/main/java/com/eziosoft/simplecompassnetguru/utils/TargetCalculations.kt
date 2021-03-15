package com.eziosoft.simplecompassnetguru.utils

import android.location.Location

object TargetCalculations {
    fun calculateBearing(currentLocation: Location, targetLocation: Location): Float =
        currentLocation.bearingTo(targetLocation)

    fun calculateDistance(currentLocation: Location, targetLocation: Location): Float =
        currentLocation.distanceTo(targetLocation)

}