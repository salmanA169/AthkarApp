package com.athkar.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.location.LocationManager
import android.util.Log

abstract class AndroidSensor(
    private val context: Context,
    private val sensorType: Int,
    private val sensorFeature: String
) : android.hardware.SensorEventListener {
    private val doseSensorExist = context.packageManager.hasSystemFeature(sensorFeature)
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    var sensorEventListener: SensorEventListener? = null


    fun startListing() {
        if (!doseSensorExist) {
            sensorEventListener?.onEventSensorChange(null, "this feature $sensorType ")
            return
        }
        sensorManager = context.getSystemService(SensorManager::class.java)
        if (sensor == null) {
            sensor = sensorManager.getDefaultSensor(sensorType)
        }
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    fun stopListen() {
        sensorManager.unregisterListener(this, sensor ?: return)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == sensorType){
            sensorEventListener?.onEventSensorChange(event.values?.toList(),null)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}