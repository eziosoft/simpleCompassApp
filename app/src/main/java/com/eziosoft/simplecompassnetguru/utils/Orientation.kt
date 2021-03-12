package com.eziosoft.simplecompassnetguru.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

//TODO should implement lifecycle
class Orientation(
    private val sensorManager: SensorManager,
    private val orientationListener: OrientationListener?
) :
    SensorEventListener {
    private val rotationVectorSensor: Sensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) }
    private val SENSOR_TYPE = Sensor.TYPE_ROTATION_VECTOR
    private var rotMat = FloatArray(9)
    private var vals = FloatArray(3)
    private var azimuth = 0.0
    private var pitch = 0.0
    private var roll = 0.0

    fun start(samplingSlow: Boolean) {
        sensorManager.registerListener(
            this,
            rotationVectorSensor,
            if (samplingSlow) 250000 else 10000
        )
    }

    fun stop() {
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
                    SensorManager.AXIS_X, SensorManager.AXIS_Y,
                    rotMat
                )
            SensorManager.getOrientation(rotMat, vals)
            azimuth = Math.toDegrees(vals[0].toDouble()) // in degrees [-180, +180]
            pitch = Math.toDegrees(vals[1].toDouble())
            roll = Math.toDegrees(vals[2].toDouble())
            orientationListener?.onSensorChanged(azimuth, pitch, roll)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    interface OrientationListener {
        fun onSensorChanged(azimuth: Double, pitch: Double, roll: Double)
    }
}
