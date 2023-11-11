package com.fishinspace.projectcosmichamster

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.database.DatabaseReference

class ChatBackgroundService : Service() {

    private lateinit var database: DatabaseReference

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Service", "Service Started!!")

        //  create service as foreground
        /*createNotificationChannel(id = "0",
            "foreground service notifier")
        startForeground(1, buildNotification(user = "sirius", "app running in background",
        id = "0"))*/
        /*val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Handle new chat messages here
                // You can send a broadcast to notify your app
                val newMessageIntent = Intent("new_chat_message")
                sendBroadcast(newMessageIntent)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors if needed
            }
        }

        //  create a new messages listener so that all new messages
        //  have a single listener
        //  ***********

        database.addValueEventListener(eventListener)*/
        //database = appViewModel.mainRef
        //database.addChildEventListener(unreadListener)
        // Return START_STICKY to keep the service running
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

class NewMessageReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "new_chat_message") {
            // Handle the new chat message, e.g., show a notification
        }
    }
}