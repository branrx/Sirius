package com.fishinspace.projectcosmichamster

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fishinspace.projectcosmichamster.ui.addToZone
import com.fishinspace.projectcosmichamster.ui.removeFromZone
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    // ...
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("broadcast", "fencing")
        val geofencingEvent = intent?.let { GeofencingEvent.fromIntent(it) }
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.errorCode)
                Log.e("broadcast", errorMessage)
                return
            }
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent?.geofenceTransition

        geofencingEvent?.triggeringGeofences?.forEach {
            Log.d("fenced up", it.requestId.toString())
        }

        var transitionStatus = ""

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
        {
            transitionStatus = "Entry"
        }   else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
        {
            transitionStatus = "Exit"
        }
        Log.d("test 1", geofenceTransition.toString())
        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
        geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL)
        {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            triggeringGeofences.first().requestId
            //appViewModel.updateGeofence("$transitionStatus : ${triggeringGeofences.first().requestId}")
            Log.e("fence id", triggeringGeofences.first().requestId)

            //  decide if entering or leaving zone
            if(transitionStatus =="Entry")
            {
                //  if multiple zones were triggered
                geofencingEvent.triggeringGeofences?.forEach {
                    addToZone(it.requestId)
                }
            }   else if(transitionStatus== "Exit")
            {
                geofencingEvent.triggeringGeofences?.forEach {
                    removeFromZone(it.requestId)
                }
            }

            /*// Get the transition details as a String.
            val geofenceTransitionDetails = getGeofenceTransitionDetails(
                this,
                geofenceTransition,
                triggeringGeofences
            )*/

            // Send notification and log the transition details.
            //sendNotification(geofenceTransitionDetails)
            //Log.i("broadcast", geofenceTransitionDetails)
        } else {
            // Log the error.
            val triggeringGeofences = geofencingEvent?.triggeringGeofences
            triggeringGeofences?.first()?.requestId
            //appViewModel.updateGeofence("$transitionStatus : ${triggeringGeofences.first().requestId}")
            if (triggeringGeofences != null) {
                Log.e("fence id", triggeringGeofences.first().requestId)
            }
            Log.d("broadcast", "not if")
        }
    }
}

fun createNotificationChannel(id: String, channelName: String)
{
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val descriptionText = "shows if discovered true"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, channelName, importance).apply {
            description = descriptionText
        }

        // Register the channel with the system
        val notificationManager: NotificationManager =
            activityContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun buildNotification(user: String, msg: String, id: String): Notification
{
    val builder = NotificationCompat.Builder(activityContext, id)
        .setSmallIcon(R.drawable.cirqle_logo_3)
        .setContentTitle(user)
        .setContentText(msg)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Set the intent that will fire when the user taps the notification
        .setAutoCancel(true)
    return builder.build()
}

fun notifier(builder: Notification)
{
    with(NotificationManagerCompat.from(activityContext)) {
        // notificationId is a unique int for each notification that you must define
        if (ActivityCompat.checkSelfPermission(
                activityContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("notifier", "notifaction sent")
            notify(0, builder)
        }
    }
}