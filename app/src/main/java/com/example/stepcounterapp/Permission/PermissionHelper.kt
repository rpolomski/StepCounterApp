package com.example.stepcounterapp.Permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(private val activity: Activity) {

    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }

    fun checkPermissions(onPermissionsChecked: (Boolean) -> Unit) {
        val permissionStatus = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACTIVITY_RECOGNITION)
        onPermissionsChecked(permissionStatus == PackageManager.PERMISSION_GRANTED)
    }


    fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
            PERMISSION_REQUEST_CODE
        )
    }
}
