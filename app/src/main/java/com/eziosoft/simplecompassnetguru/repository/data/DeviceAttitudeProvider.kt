/*
 * Created by Bartosz Szczygiel on 3/31/21 9:35 PM
 *  Copyright (c) 2021 . All rights reserved.
 *  Last modified 3/30/21 9:52 PM
 */

package com.eziosoft.simplecompassnetguru.repository.data

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.eziosoft.simplecompassnetguru.utils.TAG
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@Singleton
class DeviceAttitudeProvider(
    private val sensorManager: SensorManager
) {
    private val SENSOR_TYPE = Sensor.TYPE_ROTATION_VECTOR

    private val rotationVectorSensor: Sensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) }
    private var rotMat = FloatArray(9)
    private var vals = FloatArray(3)


    val attitude = attitudeFlow()


    private fun attitudeFlow() = callbackFlow<Attitude> {
        val callback = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == SENSOR_TYPE) {
                    SensorManager.getRotationMatrixFromVector(
                        rotMat,
                        event.values
                    )
                    SensorManager
                        .remapCoordinateSystem(
                            rotMat,
                            SensorManager.AXIS_X,
                            SensorManager.AXIS_Y,
                            rotMat
                        )
                    SensorManager.getOrientation(rotMat, vals)
                    val azimuth = Math.toDegrees(vals[0].toDouble()) // in degrees [-180, +180]
                    val pitch = Math.toDegrees(vals[1].toDouble())
                    val roll = Math.toDegrees(vals[2].toDouble())

                    sendBlocking(Attitude(azimuth, pitch, roll))
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            }

        }

        Log.d(TAG, "start: AttitudeProvider")
        sensorManager.registerListener(
            callback,
            rotationVectorSensor, 10000
        )

        awaitClose {
            Log.d(TAG, "stop: AttitudeProvider ")
            sensorManager.unregisterListener(callback)
        }

    }


    data class Attitude(val azimuth: Double, val pitch: Double, val roll: Double)


}
