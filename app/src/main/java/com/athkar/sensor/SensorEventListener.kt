package com.athkar.sensor

interface SensorEventListener {
    fun onEventSensorChange(value: List<Float>?, onSensorNotExist: String?)
}