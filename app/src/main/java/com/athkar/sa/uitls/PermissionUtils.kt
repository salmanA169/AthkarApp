package com.athkar.sa.uitls

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


fun Fragment.shouldShowLocationPermissionDialog(): Boolean {
    return shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) && shouldShowRequestPermissionRationale(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
}
fun Context.checkLocationPermission(vararg permissions:String):Boolean{
    return permissions.all {
        ContextCompat.checkSelfPermission(this,it) == PackageManager.PERMISSION_GRANTED
    }
}