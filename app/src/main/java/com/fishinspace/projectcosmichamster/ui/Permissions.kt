package com.fishinspace.projectcosmichamster.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.fishinspace.projectcosmichamster.activityContext

var pages = mutableMapOf<Int, String>(0 to "Fine Location",
    1 to "Read Media Image",
    2 to "Post Notifications",
    3 to "Background location"
)

var pageDescriptions = mutableMapOf<Int, String>(0 to "This application requires fine location, inorder to track which zone you are in.",
    1 to "This application requires permission to access images from external storage, used only while setting a profile picture.",
    2 to "This application uses post notifications to notify you of zone entries and exits.",
    3 to "This application uses background location to notify you of zone entries and exits.")

var rwPermission = ""

fun checkPermission(permission: String): Boolean
{
    if (ActivityCompat.checkSelfPermission(
            activityContext,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        Log.d("permission", "false")
        return false
    }
    return true
}

fun initPermissions(): Boolean
{
    var permission = ""
    for(index in 0..3)
    {
        when(index)
        {
            0 -> { permission = Manifest.permission.ACCESS_FINE_LOCATION }
            1 -> { permission =
                if(Build.VERSION.SDK_INT>=33)
                {
                    Manifest.permission.READ_MEDIA_IMAGES
                }   else{
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }    // checks sdk version and assigns appropriate permission request
            }
            2 -> { permission = Manifest.permission.POST_NOTIFICATIONS }
            3 -> {if(Build.VERSION.SDK_INT>=33) { permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION } }
        }

        //  check if permission is allowed
        if(checkPermission(permission))
        {
            Log.d("$permission", "allowed")
        }else{
            //  if a single permission is not allowed return false
            Log.d("$permission", "not-allowed")
            return false
        }
    }

    //  if all permissions are allowed return true
    return true
}