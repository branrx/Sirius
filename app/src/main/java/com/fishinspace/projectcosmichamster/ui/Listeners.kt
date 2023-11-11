package com.fishinspace.projectcosmichamster.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.fishinspace.projectcosmichamster.appViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue

val requestsInListener = object: ChildEventListener
{
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("requestListener", "onChildAdded:" + snapshot.key!! + snapshot.value)
        appViewModel.requestsInKeys.add(snapshot.key!!)
        appViewModel.requestsInMap[snapshot.key!!] = snapshot.value.toString()
        appViewModel.populateRequestsDetail()
        //appViewModel.requestsCount()
        //appViewModel.updateUiRequester()
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("requestListener", "onChildChanged:" + snapshot.key!!)
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d("requestListener", "onChildRemoved:" + snapshot.value!!)
        appViewModel.removeRequestIn(snapshot.value!!.toString())
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("requestListener", "onCancelled:" + error.message)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("requestListener", "onChildMoved:" + snapshot.value!!)
    }
}

val requestsOutListener = object: ChildEventListener
{
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("requestsOutListener", "onChildAdded:" + snapshot.key!! + snapshot.value)
        appViewModel.requestsOutKeys.add(snapshot.key!!)
        appViewModel.requestsOutMap[snapshot.key!!] = snapshot.value.toString()
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("requestsOutListener", "onChildChanged:" + snapshot.key!!)
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d("requestsOutListener", "onChildRemoved:" + snapshot.value!!)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("requestsOutListener", "onCancelled:" + error.message)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("requestsOutListener", "onChildMoved:" + snapshot.value!!)
    }
}

val requestsRejectedListener = object: ChildEventListener
{
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("requestsRejectedListener", "onChildAdded:" + snapshot.key!! + snapshot.value)
        appViewModel.requestsRejected.add(snapshot.value!!.toString())
        appViewModel.requestsRejectedMap[snapshot.key!!] = snapshot.value!!.toString()
        appViewModel.clearRejected(snapshot.value!!.toString(), snapshot.key!!)
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("requestsRejectedListener", "onChildChanged:" + snapshot.key!!)
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d("requestsRejectedListener", "onChildRemoved:" + snapshot.value!!)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("requestsRejectedListener", "onCancelled:" + error.message)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("requestsRejectedListener", "onChildMoved:" + snapshot.value!!)
    }
}

val friendsListener = object: ChildEventListener
{
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("friendsListener", "onChildAdded:" + snapshot.key!! + snapshot.value)
        appViewModel.friendsKeys.add(snapshot.key!!)
        appViewModel.friendsMap[snapshot.key!!] = snapshot.value!!.toString()
        appViewModel.getFriendDetails(snapshot.value!!.toString(), snapshot.key!!)
        appViewModel.attachMessageListeners(snapshot.value!!.toString())
        appViewModel.friendsCount()
        Log.d("friends count", appViewModel.friendsCount.toString())
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("friendsListener", "onChildChanged:" + snapshot.key!!)
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d("friendsListener", "onChildRemoved:" + snapshot.value!!)
        appViewModel.onUnfriend(snapshot.value.toString())
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("friendsListener", "onCancelled:" + error.message)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("friendsListener", "onChildMoved:" + snapshot.value!!)
    }
}

val unfriendListener = object: ChildEventListener
{
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("unFriendListener", "onChildAdded:" + snapshot.key!! + snapshot.value)
        appViewModel.unfriendKeys.add(snapshot.key!!)
        appViewModel.unfriendMap[snapshot.key!!] = snapshot.value!!.toString()

        //  clear from friends list
        appViewModel.clearUnfriend(userID = snapshot.value!!.toString(), unfriendKey = snapshot.key!!)
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("unFriendListener", "onChildChanged:" + snapshot.key!!)
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d("unFriendListener", "onChildRemoved:" + snapshot.value!!)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("unFriendListener", "onCancelled:" + error.message)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("unFriendListener", "onChildMoved:" + snapshot.value!!)
    }
}

val unreadListener = object: ChildEventListener
{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        var date  = snapshot.key
        var msg = ""
        var sender = ""
        var msgData = mutableMapOf<String, String>()
        snapshot.children.forEach {data->
            msgData[data.key.toString()] = data.value.toString()
        }
        Log.d("unreadListener", "message userid: " + msgData["id"])
        msgData["date"] = date.toString()

        //  create temp message class object
        var tempMsg = MessageClass(sender = msgData["sender"].toString(), date = date.toString(),
            message = msgData["msg"].toString(), post = msgData["post"].toString())

        //  save messages to map
        var userID = msgData["id"].toString()
        var tempList = appViewModel.messagesUnreadMap.value

        //  set messages as new highlight
        appViewModel.setHighlight(userID, tempMsg)

        try{
            tempList.messagesUnreadDetails[userID]!!.add(tempMsg)
        }
        catch (e:Exception){
            tempList.messagesUnreadDetails[userID] = mutableListOf()
            tempList.messagesUnreadDetails[userID]!!.add(tempMsg)
        }

        try{ appViewModel.getMsgs() }
        catch (e:Exception){}

        if(appViewModel.openChat.value.chatID==userID)
        {
            appViewModel.clearUnread(userID)
        }
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("unreadListener", "onMessageAdded:" + snapshot.value!!)
        var superUID = ""
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d("unreadListener", "onChildRemoved:" + snapshot.value!!)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("unreadListener", "onCancelled:" + error.message)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("unreadListener", "onChildMoved:" + snapshot.value!!)
    }
}

val readListener = object: ChildEventListener
{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        var date  = snapshot.key
        var msg = ""
        var sender = ""
        var msgData = mutableMapOf<String, String>()
        snapshot.children.forEach {data->
            msgData[data.key.toString()] = data.value.toString()
        }
        Log.d("readListener", "message userid: " + msgData["id"])
        msgData["date"] = date.toString()

        //  create temp message class object
        var tempMsg = MessageClass(sender = msgData["sender"].toString(), date = date.toString(),
            message = msgData["msg"].toString(), post = msgData["post"].toString())

        //  save messages to map
        var userID = msgData["id"].toString()
        var tempList = appViewModel.messagesMap.value
        //tempList.messagesDetails[userID]!!.add(tempMsg)

        try{
            tempList.messagesDetails[userID]!!.add(tempMsg)
        }
        catch (e:Exception){
            tempList.messagesDetails[userID] = mutableListOf()
            tempList.messagesDetails[userID]!!.add(tempMsg)
        }

        //  clear messages from unread
        var ref = appViewModel.dbObject.reference.child("users").child(appViewModel.currentUserUid).child("chats")
            .child(userID).child("unread").child(date.toString()).setValue(null)

        if(appViewModel.openChat.value.chatID==userID)
        {
            appViewModel.updateOpenChat(userID)
        }

        try{ appViewModel.getMsgs() }
        catch (e:Exception){}

    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("readListener", "onMessageAdded:" + snapshot.value!!)
        var superUID = ""
        //appViewModel.messagesMap.clear()
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d("unreadListener", "onChildRemoved:" + snapshot.value!!)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("unreadListener", "onCancelled:" + error.message)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("unreadListener", "onChildMoved:" + snapshot.value!!)
    }
}

//  responsible for notfications of incoming messages
val newMessagesListener = object: ChildEventListener
{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        var userID  = snapshot.key.toString()
        var msg = ""

        var msgData = mutableMapOf<String, String>()

        //  new varibles
        var time = ""
        var message = ""
        var sender = ""

        var newref = appViewModel.dbObject.reference.child("users").child(appViewModel.currentUserUid)
            .child("chats").child("new messages").child(userID)

        snapshot.children.forEach {timeObj ->
            // date
            time = timeObj.key.toString()
            timeObj.children.forEach {
                //  message data    //  another entry should be post
                msgData[it.key.toString()] = it.value.toString()
            }

            //  send a notification
            //val notificationBuilder = buildNotification(userID, msgData["msg"].toString(), "1")
            //notifier(notificationBuilder)

            //  save
            //  move the message into unread messages
            var ref = appViewModel.dbObject.reference.child("users").child(appViewModel.currentUserUid)
                .child("chats").child(userID).child("unread").child(time).setValue(msgData)

            //  remove the notified messages from new messages buffer
            appViewModel.dbObject.reference.child("users").child(appViewModel.currentUserUid).child("chats")
                .child("new messages").child(userID).child(time).setValue(null)
        }
        Log.d("newMessagesListener", "message userid: " + msgData["id"])
        //msgData["date"] = time

        //  save messages to map
        //var userID = msgData["id"].toString()

        //  trigger the notification
        //  ***
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("newMessagesListener", "onMessageAdded:" + snapshot.value!!)
        var superUID = ""
        //appViewModel.messagesMap.clear()
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d("newMessagesListener", "onChildRemoved:" + snapshot.value!!)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("newMessagesListener", "onCancelled:" + error.message)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("newMessagesListener", "onChildMoved:" + snapshot.value!!)
    }
}

val exploreListener = object: ChildEventListener
{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("explored key", snapshot.key.toString())
        appViewModel.discoveredPool.add(snapshot.key.toString())
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("messagesListener", "onMessageAdded:" + snapshot.value!!)
        var superUID = ""
        //appViewModel.messagesMap.clear()
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d("messagesListener", "onChildRemoved:" + snapshot.value!!)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("messagesListener", "onCancelled:" + error.message)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("messagesListener", "onChildMoved:" + snapshot.value!!)
    }
}

val discoveredListener = object: ChildEventListener
{
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("discoveredListener", "onChildAdded:" + snapshot.key!! + snapshot.value)
        appViewModel.isDiscoverLoading = true
        appViewModel.timerDiscoverLoading += 1
        appViewModel.populateDiscovery(snapshot.value.toString(), snapshot.key!!)
        appViewModel.discoveredCount = appViewModel.friendsDetailList.value.friendsCount
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("discoveredListener", "onChildChanged:" + snapshot.key!!)
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d("discoveredListener", "onChildRemoved:" + snapshot.value!!)
        appViewModel.removeDiscovered(snapshot.value!!.toString())
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("discoveredListener", "onCancelled:" + error.message)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("discoveredListener", "onChildMoved:" + snapshot.value!!)
    }
}

val postsListener = object: ChildEventListener
{
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("postsListener", "onChildAdded:" + snapshot.key!! + snapshot.value)
        appViewModel.isPostsLoading = true
        appViewModel.timerPostsLoading += 1
        var note = ""
        val userID = snapshot.key.toString()
        var time = ""
        var cat = ""
        var specifier = ""

        note = snapshot.child("note").value.toString()
        cat = snapshot.child("category").value.toString()
        specifier = snapshot.child("specifier").value.toString()
        time = snapshot.child("time").value.toString()

        //  get username and gender
        val ref = appViewModel.dbObject.reference.child("users").child(userID).child("publicInfo")
        var username = ""
        var gender = ""

        ref.child("username").get().addOnSuccessListener { nameObj ->
            username = nameObj.getValue<String>().toString()
            ref.child("gender").get().addOnSuccessListener {genderObj->
                gender = genderObj.getValue<String>().toString()
                val tempPost = PostClass(userID = userID, note = note, time = time.toLong(), cat = cat, specifier = specifier,
                username = username, gender = gender)
                appViewModel.addPost(tempPost)
            }
        }
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("postsListener", "onChildChanged:" + snapshot.key!!)
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d("postsListener", "onChildRemoved:" + snapshot.value!!)
        appViewModel.removePost(snapshot.key!!.toString())
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("postsListener", "onCancelled:" + error.message)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("postsListener", "onChildMoved:" + snapshot.value!!)
    }
}

val availableMinorListener = object: ChildEventListener
{
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("availableMinorListener", "onChildAdded:" + snapshot.key!! + snapshot.value)
        val zoneid = snapshot.key.toString()
        val lat = snapshot.child("lat").value.toString().toDouble()
        val lng = snapshot.child("lng").value.toString().toDouble()
        val radius = snapshot.child("radius").value.toString().toFloat()
        val geoFenceObj = getGeofence(zoneid, lat, lng, radius)
        buildFences(geoFenceObj)
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("availableMinorListener", "onChildChanged:" + snapshot.key!!)
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        Log.d("availableMinorListener", "onChildRemoved:" + snapshot.value!!)
        //removeGeofence(listOf(snapshot.key.toString()))
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("availableMinorListener", "onCancelled:" + error.message)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("availableMinorListener", "onChildMoved:" + snapshot.value!!)
    }
}

