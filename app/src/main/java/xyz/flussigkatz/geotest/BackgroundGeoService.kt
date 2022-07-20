package xyz.flussigkatz.geotest

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

private const val LOCATION_REFRESH_TIME = 1L
private const val LOCATION_REFRESH_DISTANCE = 1f

class BackgroundGeoService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setUpLocationListener()
        return START_STICKY
    }

    private fun setUpLocationListener() {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val formatter = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault())
        val locationListener = LocationListener { loc ->
            println("!!!${formatter.format(loc.time)}, ${loc.latitude}, ${loc.longitude}!!!")
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_REFRESH_TIME,
            LOCATION_REFRESH_DISTANCE,
            locationListener
        )
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }

}