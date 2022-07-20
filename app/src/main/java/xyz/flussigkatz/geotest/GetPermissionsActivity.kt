package xyz.flussigkatz.geotest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import xyz.flussigkatz.geotest.AppConst.ACTION_PERMISSIONS_DENIED
import xyz.flussigkatz.geotest.AppConst.ACTION_PERMISSIONS_GRANTED

class GetPermissionsActivity : AppCompatActivity() {
    private val permissionRequestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selfPermission = intArrayOf(
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
        ).all { it == PackageManager.PERMISSION_DENIED }
        if (selfPermission) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
                permissionRequestCode
            )
        } else finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionRequestCode) {
            val permissionGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (permissionGranted) sendBroadcast(Intent(ACTION_PERMISSIONS_GRANTED))
            else sendBroadcast(Intent(ACTION_PERMISSIONS_DENIED))
            finish()
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}