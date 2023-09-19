package com.fishinspace.projectcosmichamster

import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import java.util.concurrent.TimeUnit

var locationRequest = LocationRequest.create().apply {
    // Sets the desired interval for active location updates. This interval is inexact. You
    // may not receive updates at all if no location sources are available, or you may
    // receive them less frequently than requested. You may also receive updates more
    // frequently than requested if other applications are requesting location at a more
    // frequent interval.
    //
    // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
    // targetSdkVersion) may receive updates less frequently than this interval when the app
    // is no longer in the foreground.
    interval = TimeUnit.SECONDS.toMillis(5)

    // Sets the fastest rate for active location updates. This interval is exact, and your
    // application will never receive updates more frequently than this value.
    fastestInterval = TimeUnit.SECONDS.toMillis(10)

    // Sets the maximum time when batched location updates are delivered. Updates may be
    // delivered sooner than this interval.
    maxWaitTime = TimeUnit.SECONDS.toMillis(15)

    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
}

var locationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
        super.onLocationResult(locationResult)

        // Normally, you want to save a new location to a database. We are simplifying
        // things a bit and just saving it as a local variable, as we only need it again
        // if a Notification is created (when the user navigates away from app).
        currentLocation = locationResult.lastLocation

        // Notify our Activity that a new location was added. Again, if this was a
        // production app, the Activity would be listening for changes to a database
        // with new locations, but we are simplifying things a bit to focus on just
        // learning the location side of things.
        var coords = "${currentLocation?.latitude.toString()}:${currentLocation?.longitude.toString()}"
        Log.d("location", coords)
    }
}



var geofenceList = mutableListOf(
    Geofence.Builder()
// Set the request ID of the geofence. This is a string to identify this
// geofence.
.setRequestId("122")

// Set the circular region of this geofence.
.setCircularRegion(
    35.12547,
    32.83916,
500f
)

// Set the expiration duration of the geofence. This geofence gets automatically
// removed after this period of time.
.setExpirationDuration(120000)

// Set the transition types of interest. Alerts are only generated for these
// transition. We track entry and exit transitions in this sample.
.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)

// Create the geofence.
.build())