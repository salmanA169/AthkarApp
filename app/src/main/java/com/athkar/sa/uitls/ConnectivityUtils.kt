package com.athkar.sa.uitls

import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.core.net.ConnectivityManagerCompat

fun ConnectivityManager.checkConnection():Boolean{
   return activeNetwork!=null
}