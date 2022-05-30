package com.athkar.sa.sensor

interface SensorEventListener {
    fun onEventSensorChange(value: List<Float>?, onSensorNotExist: String?)
}