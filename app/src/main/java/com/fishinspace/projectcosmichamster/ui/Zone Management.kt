package com.fishinspace.projectcosmichamster.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.fishinspace.projectcosmichamster.GeofenceBroadcastReceiver
import com.fishinspace.projectcosmichamster.activityThis
import com.fishinspace.projectcosmichamster.appViewModel
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction

fun addToZone(zoneID: String)
{
    var userID = ""
    try
    {
        userID = if(appViewModel.dbAuth.currentUser==null){return}else{appViewModel.dbAuth.currentUser!!.uid}
    }   catch (e: Exception)
    {
        Log.d("add to zone", e.message.toString())
        return
    }
    var zoneType = zoneID.split("*")

    when(zoneType[0])
    {
        "mj" -> {
            var majorID = zoneType[1]
            appViewModel.currentMajorZone = zoneID
            var ref = appViewModel.dbObject.reference.child("zones").child(majorID)
            ref.child("users").child(userID).setValue(userID)

            var tempRef = appViewModel.dbObject.reference.child("users").child(userID).child("zones").child("major")
            tempRef.setValue(majorID)

            //  attach listener
            appViewModel.attachMJListener(ref)
            Log.d("Posts ListAttach", "MJ attachment success")
        }
        "mn" -> {
            var majorID = zoneType[1]
            var minorID = zoneType[2]
            appViewModel.currentMinorZone = "mn*${majorID}*${minorID}"
            var ref = appViewModel.dbObject.reference.child("zones").child(majorID).child("minors").child(minorID)
            ref.child("users").child(userID).setValue(userID)

            //  update which zone user is in
            var tempRef = appViewModel.dbObject.reference.child("users").child(userID).child("zones").child("minor")
            tempRef.setValue(minorID)
            // attach listener
            ref.child("users").addChildEventListener(discoveredListener)
        }
    }
}

fun removeFromZone(zoneID: String)
{
    var userID = ""
    try
    {
        userID = if(appViewModel.dbAuth.currentUser==null){return}else{appViewModel.dbAuth.currentUser!!.uid}
    }   catch (e: Exception)
    {
        Log.d("remove from zone", e.message.toString())
        return
    }

    var zoneType = zoneID.split("*")

    when(zoneType[0])
    {
        "mj" -> {
            appViewModel.currentMajorZone = ""
            var majorID = zoneType[1]
            var ref = appViewModel.dbObject.reference.child("zones").child(majorID)
            ref.child("users").child(userID).setValue(null)

            //  update which zone user is in
            var tempRef = appViewModel.dbObject.reference.child("users").child(userID).child("zones").child("major")
            tempRef.setValue(null)

            //  remove listener
            ref.removeEventListener(postsListener)

            appViewModel.currentMajorZone = ""
        }
        "mn" -> {
            var majorID = zoneType[1]
            var minorID = zoneType[2]
            var ref = appViewModel.dbObject.reference.child("zones").child(majorID).child("minors").child(minorID)
            ref.child("users").child(userID).setValue(null)

            //  update which zone user is in
            var tempRef = appViewModel.dbObject.reference.child("users").child(userID).child("zones").child("minor")
            tempRef.setValue(null)
        }
    }
}

//  define fence coords
var fencesTempMN = MinorZoneClass(id = "0", lat = 35.12547, lng = 32.83916, radius = 100F, location = "ekici", majorID = "0", type = "mn")

var fencesTempMJ = MajorZoneClass(id = "0", lat = 35.12563, lng = 32.84009, radius = 500F, location = "lefke", type = "mj")

//  map of id to coords
fun setupFences(/*fencesList: MutableList<Map<String, Double>>*/)
{
    var tempGeofences = mutableListOf<Geofence>()

    tempGeofences.add(getGeofence(
        fenceID = "${fencesTempMN.type}*${fencesTempMN.majorID}*${fencesTempMN.id}",
        lat = fencesTempMN.lat,
        lng = fencesTempMN.lng,
        radius = fencesTempMN.radius,
    ))

    tempGeofences.add(getGeofence(
        fenceID = "${fencesTempMJ.type}*${fencesTempMJ.id}",
        lat = fencesTempMJ.lat,
        lng = fencesTempMJ.lng,
        radius = fencesTempMJ.radius,
    ))

    Log.d("fence count", tempGeofences.size.toString())
    //buildFences(tempGeofences)
}

@SuppressLint("VisibleForTests")
fun getGeofence(fenceID: String, lat: Double, lng: Double, radius: Float): Geofence
{
    val geofence = Geofence.Builder()
        .setRequestId(fenceID)
        .setCircularRegion(
            lat,
            lng,
            radius
        )
        .setExpirationDuration(Geofence.NEVER_EXPIRE)
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
        .build()

    return geofence
}

//  prepare intent
val geofencePendingIntent: PendingIntent by lazy {
    val intent = Intent(activityThis, GeofenceBroadcastReceiver::class.java)
    PendingIntent.getBroadcast(activityThis, 0, intent, PendingIntent.FLAG_MUTABLE)
}

fun buildFences(geofenceObj: Geofence)
{
    //  build fences
    val geofenceRequest = GeofencingRequest.Builder().apply {
        setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        addGeofence(geofenceObj)
    }.build()

    initGeofences(geofenceRequest, geofencePendingIntent)
}

fun initGeofences(geofenceRequest: GeofencingRequest, geofencePendingIntent: PendingIntent)
{
    if (ActivityCompat.checkSelfPermission(
            activityThis,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.

        appViewModel.geofencingClient.addGeofences(geofenceRequest, geofencePendingIntent)?.run {
            addOnSuccessListener {
                Log.d("add fence", "successfully added fence")
                // ...
            }
            addOnFailureListener {
                // Failed to add geofences
                Log.d("add fence", it.toString())
                // ...
            }
        }
    }
}

fun removeGeofence(geofenceList: List<String>)
{
    //geofencingClient.
    //var tempList = listOf("mn*0*0", "mj*0")
    appViewModel.geofencingClient.removeGeofences(geofenceList).run {
        addOnSuccessListener {
            Log.d("successfully removed fence", this.isComplete.toString())
        }
        addOnFailureListener {
            Log.d("failed to remove fence", it.message.toString())
        }
    }
}

fun uploadMinorZone(zone: MinorZoneClass)
{
    val zoneid = "mn*${zone.majorID}*${zone.id}"
    val ref = appViewModel.dbObject.reference.child("available zones").child("minor")
    val zoneObj = mapOf("id" to zone.id, "type" to zone.type, "lat" to zone.lat, "lng" to zone.lng,
        "location" to zone.location, "majorID" to zone.majorID, "radius" to zone.radius)

    Log.d("uploadMinorZone transaction", "in function")

    ref.runTransaction(object: Transaction.Handler{
        override fun doTransaction(currentData: MutableData): Transaction.Result {
            currentData.child(zoneid).value = zoneObj

            return Transaction.success(currentData)
        }

        override fun onComplete(
            error: DatabaseError?,
            committed: Boolean,
            currentData: DataSnapshot?
        ) {
            Log.d("uploadMinorZone transaction", committed.toString())
            Log.d("uploadMinorZone transaction", error?.message.toString())
        }
    })
}

fun uploadMajorZone(zone: MajorZoneClass)
{
    val zoneid = "mj*${zone.id}"
    val ref = appViewModel.dbObject.reference.child("available zones").child("major")
    val zoneObj = mapOf("id" to zone.id, "type" to zone.type, "lat" to zone.lat, "lng" to zone.lng,
        "location" to zone.location, "radius" to zone.radius)

    Log.d("uploadMajorZone transaction", "in function")

    ref.runTransaction(object: Transaction.Handler{
        override fun doTransaction(currentData: MutableData): Transaction.Result {
            currentData.child(zoneid).value = zoneObj

            return Transaction.success(currentData)
        }

        override fun onComplete(
            error: DatabaseError?,
            committed: Boolean,
            currentData: DataSnapshot?
        ) {
            Log.d("uploadMajorZone transaction", committed.toString())
            Log.d("uploadMajorZone transaction", error?.message.toString())
        }
    })
}

