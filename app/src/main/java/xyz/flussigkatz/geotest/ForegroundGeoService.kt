package xyz.flussigkatz.geotest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

private const val NOTIFICATION_CHANNEL_ID = "notification_channel_1"
private const val NOTIFICATION_CHANNEL_NAME = "Foreground Geo Service"
private const val NOTIFICATION_TITLE = "GeoService"
private const val SERVICE_ID = 1
private const val LOCATION_REFRESH_TIME = 1L
private const val LOCATION_REFRESH_DISTANCE = 1f

class ForegroundGeoService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(SERVICE_ID, initNotification(this))
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

    private fun initNotification(context: Context): Notification {
        val notification: Notification.Builder
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_MIN
            )
            notificationManager.createNotificationChannel(nChannel)
            notification = Notification.Builder(
                context,
                NOTIFICATION_CHANNEL_ID
            )
        } else {
            @Suppress("DEPRECATION")
            notification = Notification.Builder(context)
        }
        return notification.setContentTitle(NOTIFICATION_TITLE)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_location)
            .build()
    }
}