package xyz.flussigkatz.geotest

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import xyz.flussigkatz.geotest.AppConst.ACTION_PERMISSIONS_DENIED
import xyz.flussigkatz.geotest.AppConst.ACTION_PERMISSIONS_GRANTED

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter().also {
            it.addAction(ACTION_PERMISSIONS_GRANTED)
            it.addAction(ACTION_PERMISSIONS_DENIED)
        }
        registerReceiver(PermissionReceiver(), intentFilter)
    }

    private inner class PermissionReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_PERMISSIONS_GRANTED -> {
                    context.unregisterReceiver(this)
                    onPermissionsGranted()
                }
                ACTION_PERMISSIONS_DENIED -> {
                    context.unregisterReceiver(this)
                    onPermissionsDenied()
                }
                else -> throw IllegalArgumentException("Invalid action")
            }
        }
    }

    private fun onPermissionsGranted() {
//        startService(Intent(this, BackgroundGeoService::class.java))
        startForegroundService(Intent(this, ForegroundGeoService::class.java))
    }

    private fun onPermissionsDenied() {
        Toast.makeText(
            this@App,
            "The application requires access to geolocation.",
            Toast.LENGTH_LONG
        ).show()
    }
}