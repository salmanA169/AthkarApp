package com.athkar.sa.uitls

import android.net.ConnectivityManager

fun ConnectivityManager.hasNetwork():Boolean{
   return activeNetwork!=null
}