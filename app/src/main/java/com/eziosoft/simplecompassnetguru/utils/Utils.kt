package com.eziosoft.simplecompassnetguru.utils

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