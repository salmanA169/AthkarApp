package com.athkar.sa.sensor

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor

class SensorOrientation(context: Context) :
    AndroidSensor(context, Sensor.TYPE_ORIENTATION, PackageManager.FEATURE_SENSOR_COMPASS)
