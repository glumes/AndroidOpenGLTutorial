package com.glumes.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 * Created by glumes on 28/03/2018
 */
class CameraUtil {


    companion object {

        fun checkCameraPermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }

        fun requestCameraPermission() {

        }
    }
}