package com.eziosoft.simplecompassnetguru.ui.targetInputFragment

import android.location.Location
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.eziosoft.simplecompassnetguru.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TargetInputFragmentViewModel @Inject constructor(
    state: SavedStateHandle,
    val repository: Repository
) : ViewModel() {

    //TODO save coordinates for future use
    // save coordinates to state in case of process death

    fun saveCoordinates(coordinates: String) {
        if (validateCoordinates(coordinates)) {
            val a = coordinates.split(",")
            val lat = a[0].toDouble()
            val lon = a[1].toDouble()

            val location = Location("")
            location.apply {
                latitude = lat
                longitude = lon
                repository.setTargetLocation(this)
            }
        }
    }

    fun validateCoordinates(coordinates: String): Boolean {
        val regex = "^(-?\\d+(\\.\\d+)?),\\s*(-?\\d+(\\.\\d+)?)\$".toRegex()
        return if (!coordinates.matches(regex)) false
        else {
            val a = coordinates.split(",")
            val lat = a[0].toFloat()
            val lon = a[1].toFloat()
            (lat >= -90 && lat <= 90) && (lon >= -180 && lon <= 180)
        }
    }


}