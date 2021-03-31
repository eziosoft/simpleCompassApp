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
import androidx.lifecycle.*
import javax.inject.Singleton


data class Attitude(val azimuth: Double, val pitch: Double, val roll: Double)


@Singleton
class DeviceAttitudeProvider(
    private val sensorManager: SensorManager
) :
    SensorEventListener, LifecycleObserver {
    private val SENSOR_TYPE = Sensor.TYPE_ROTATION_VECTOR

    private val rotationVectorSensor: Sensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) }
    private var rotMat = FloatArray(9)
    private var vals = FloatArray(3)

    private val _attitude = MutableLiveData<Attitude>()
    val attitude: LiveData<Attitude> = _attitude

    fun addLifeCycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start() {
        Log.d("aaa", "start: AttitudeProvider")
        sensorManager.registerListener(
            this,
            rotationVectorSensor, 10000
        )
//        if (samplingSlow) 250000 else 10000
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        Log.d("aaa", "stop: AttitudeProvider")
        sensorManager.unregisterListener(this)
    }


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
            _attitude.postValue(Attitude(azimuth, pitch, roll))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

}
