package com.fishinspace.projectcosmichamster.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.scale
import androidx.lifecycle.ViewModel
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.activityContext
import com.fishinspace.projectcosmichamster.activityThis
import com.fishinspace.projectcosmichamster.auth
import com.fishinspace.projectcosmichamster.navController
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.Executors


class AppViewModel : ViewModel()
{
    private val _uiState = MutableStateFlow(AppUiState())

    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    //  friend-user variables - used by the messages screen
    private var _friendsDetailList = MutableStateFlow(FriendsClass())
    val friendsDetailList: StateFlow<FriendsClass> = _friendsDetailList.asStateFlow()
    var friendsCount by mutableStateOf(0)

    //  discovered-user variables - used by the discovery screen
    private var _discoveredDetailList = MutableStateFlow(DiscoveredClass())
    val discoveredDetailList: StateFlow<DiscoveredClass> = _discoveredDetailList.asStateFlow()
    var discoveredCount by mutableIntStateOf(0)

    //  requests-user variables - used by the requests screen
    private var _requestsInDetailList = MutableStateFlow(RequestsInClass())
    val requestsInDetailList: StateFlow<RequestsInClass> = _requestsInDetailList.asStateFlow()
    var requestsInCount by mutableStateOf(0)

    //  posts variables - used by the board screen
    private var _postsList = MutableStateFlow(PostsInClass())
    val postsList: StateFlow<PostsInClass> = _postsList.asStateFlow()
    var postsCount by mutableStateOf(0)

    //  posts variables - used by the board screen
    private var _messagesMap = MutableStateFlow(MessageReadClass())
    val messagesMap: StateFlow<MessageReadClass> = _messagesMap.asStateFlow()
    var messagesCount by mutableStateOf(0)

    //  posts variables - used by the board screen
    private var _messagesUnreadMap = MutableStateFlow(MessageUnreadClass())
    val messagesUnreadMap: StateFlow<MessageUnreadClass> = _messagesUnreadMap.asStateFlow()
    var messagesUnreadCount by mutableStateOf(0)

    //  open chat variables - used by the chat to update on message received
    private var _openChat = MutableStateFlow(OpenChatClass())
    val openChat: StateFlow<OpenChatClass> = _openChat.asStateFlow()
    var openChatMsgCount by mutableStateOf(0)

    //  message highlights - used by the chat to show last message received or sent
    private var _messageHighlights = MutableStateFlow(MessageHighlightsClass())
    val messageHighlights: StateFlow<MessageHighlightsClass> = _messageHighlights.asStateFlow()
    var _messageHighlightsCount by mutableStateOf(0)

    private val dbRoot = "https://project-cosmic-hamster-default-rtdb.europe-west1.firebasedatabase.app/"
    val dbObject = Firebase.database(dbRoot)
    val dbAuth = Firebase.auth
    val dbStorage = Firebase.storage

    //  late init variables
    lateinit var postEditCategory: String
    lateinit var postEditSpecifier: String
    lateinit var postEditNote: String

    //  current user is the one logged_in
    var currentUserUid by mutableStateOf("")
    var currentUserUserame by mutableStateOf("")
    var currentUserRealname by mutableStateOf("")
    var currentUserSurname by mutableStateOf("")
    var currentUserAge by mutableStateOf("")
    var currentUserGender by mutableStateOf("")
    var currentUserSchool by mutableStateOf("")
    var currentUserMajor by mutableStateOf("")
    var currentUserBio by mutableStateOf("")
    var currentUserYear by mutableStateOf("")

    var editUserUserame by mutableStateOf("")
    var editUserRealname by mutableStateOf("")
    var editUserSurname by mutableStateOf("")
    var editUserAge by mutableStateOf("")
    var editUserGender by mutableStateOf("")
    var editUserSchool by mutableStateOf("")
    var editUserMajor by mutableStateOf("")
    var editUserBio by mutableStateOf("")
    var editUserYear by mutableStateOf("")

    //  discovered user info
    var discoveredUserUid by mutableStateOf("")
    var discoveredUserUserame by mutableStateOf("")
    var discoveredUserRealname by mutableStateOf("")
    var discoveredUserSurname by mutableStateOf("")
    var discoveredUserAge by mutableStateOf("")
    var discoveredUserGender by mutableStateOf("")
    var discoveredUserSchool by mutableStateOf("")
    var discoveredUserMajor by mutableStateOf("")
    var discoveredUserBio by mutableStateOf("")
    var discoveredUserYear by mutableStateOf("")

    //  requester user info
    var requesterUsername by mutableStateOf("")
    var requesterAge by mutableStateOf("")
    var requesterQuote by mutableStateOf("")
    var requesterMajor by mutableStateOf("")
    var requesterSchool by mutableStateOf("")
    var requesterYear by mutableStateOf("")
    var requesterGender by mutableStateOf("")
    var requesterMapKey by mutableStateOf("")
    var requestAvailable by mutableStateOf(false)

    //  other variables
    var requestsInMap = mutableMapOf<String, String>()
    var requestsInKeys = mutableListOf<String>()
    var requestsOutMap = mutableMapOf<String, String>()
    var requestsOutKeys = mutableListOf<String>()
    var requestsRejected = mutableListOf<String>()
    var requestsRejectedMap = mutableMapOf<String, String>()
    var friendsMap = mutableMapOf<String, String>()
    var friendsKeys = mutableListOf<String>()
    var xxfriendsDetailList = mutableListOf<UserClass>()
    var unfriendMap = mutableMapOf<String, String>()
    var unfriendKeys = mutableListOf<String>()
    var discoveredList = mutableListOf<UserClass>()
    var discoveredImagesList = mutableMapOf<String, ImageBitmap>()

    //  requests list
    var requestDetailsList = mutableListOf<RequesterClass>()
    var requestImagesMap = mutableMapOf<String, ImageBitmap>()

    //  messages variables
    var unreadMap = mutableMapOf<String, MutableList<MutableMap<String, String>>>()
    var readMap = mutableMapOf<String, MutableList<MutableMap<String, String>>>()
    var activeChatID by mutableStateOf("")
    var activeChatNumber by mutableStateOf(0)

    //  users profile picture variables
    var friendProfilePictures = mutableMapOf<String, ImageBitmap>()

    //
    var unfriendRecompose by mutableStateOf(false)
    var msgCount by mutableStateOf(0)

    var imgUri: Uri? = null

    var discoveredImages by mutableStateOf<ImageBitmap>(getDefaultImage())

    var requesterImage by mutableStateOf<ImageBitmap>(getDefaultImage())

    var userProfilePicture by mutableStateOf<ImageBitmap>(getDefaultImage())

    var previousProfilePicture by mutableStateOf<ImageBitmap>(getDefaultImage())

    var isUploading by mutableStateOf(false)

    //var requestsInCount by mutableStateOf(0)

    var requestsKnown = mutableListOf<String>()

    var discoveredPool = mutableListOf<String>()

    var discoveredIndex by mutableIntStateOf(0)

    var swipeOn by mutableStateOf(true)

    var geoFenceStatus by mutableStateOf("none")

    var currentMajorZone by mutableStateOf("")
    var currentMinorZone by mutableStateOf("")

    //  active window
    var activeWindow by mutableStateOf(1)

    //  initial loading screen
    var isLoading by mutableStateOf(false)

    //  init
    init
    {
        // to do
        //_friendsDetailList.value = mutableListOf()
    }

    fun updateOpenChat(userID: String)
    {
        //  get users messages count
        try{
            openChatMsgCount = messagesMap.value.messagesDetails[userID]!!.size
        } catch (e:Exception)
        {
            openChatMsgCount = 0
        }

        _openChat.update { OpenChatClass(chatID = userID, messageCount = openChatMsgCount) }
    }

    fun addPost(post: PostClass)
    {
        var tempList = _postsList.value.postsList
        tempList.add(post)
        postsCount = tempList.size
        _postsList.update { PostsInClass(postsList = tempList, postsCount = postsCount) }
    }

    private fun addFriend(user: UserClass)
    {
        var tempList = _friendsDetailList.value.friendsDetails
        tempList.add(user)
        friendsCount = tempList.size
        _friendsDetailList.update { FriendsClass(friendsDetails = tempList, friendsCount = friendsCount) }
    }

    fun removeFriend(userID: String)
    {
        var tempList = _friendsDetailList.value.friendsDetails
        var toRemove = 0
        var count = 0
        tempList.forEach {
            if(userID == it.uid)
            {
                toRemove = count
            }
            count += 1
        }

        tempList.removeAt(toRemove)
        friendsCount = tempList.size
        _friendsDetailList.update { FriendsClass(friendsDetails = tempList, friendsCount = friendsCount) }
    }

    private fun addDiscovered(user: UserClass)
    {
        var tempList = _discoveredDetailList.value.discoveredDetails
        tempList.add(user)
        discoveredCount = tempList.size
        Log.d("disc_updated count", discoveredCount.toString())
        _discoveredDetailList.update { DiscoveredClass(discoveredDetails = tempList, discoveredCount = discoveredCount) }
    }

    fun removeDiscovered(userID: String)
    {
        var tempList = _discoveredDetailList.value.discoveredDetails
        if(tempList.isEmpty()){return}
        var toRemove = 0
        var count = 0
        tempList.forEach {
            if(userID == it.uid)
            {
                toRemove = count
            }
            count += 1
        }

        Log.d("Discover Removal", "Removing element")
        tempList.removeAt(toRemove)
        discoveredCount = tempList.size
        if(tempList.isEmpty()){discoveredCount = 0}
        _discoveredDetailList.update { DiscoveredClass(discoveredDetails = tempList, discoveredCount = discoveredCount) }
    }

    private fun addRequestIn(user: RequesterClass)
    {
        var tempList = _requestsInDetailList.value.requestsInDetails
        tempList.add(user)
        requestsInCount = tempList.size
        _requestsInDetailList.update { RequestsInClass(requestsInDetails = tempList, requestsInCount = requestsInCount) }
    }

    fun removeRequestIn(userID: String)
    {
        var tempList = _requestsInDetailList.value.requestsInDetails
        var toRemove = 0
        var count = 0
        tempList.forEach {
            if(userID == it.uid)
            {
                toRemove = count
            }
            count += 1
        }

        tempList.removeAt(toRemove)
        requestsInCount = tempList.size
        _requestsInDetailList.update { RequestsInClass(requestsInDetails = tempList, requestsInCount = requestsInCount) }
    }

    fun updateGeofence(status: String)
    {
        geoFenceStatus = status
        _uiState.update { AppUiState(
            geoFenceStatus = geoFenceStatus
        ) }
    }

    fun populateRequestsDetail()
    {
        requestsInKeys.forEach {
            var uid = requestsInMap[it].toString()
            var key = it.toString()
            if(!requestsKnown.contains(uid)){getRequester(uid, key)}
        }
    }

    fun getUserImage(): ImageBitmap
    {
        var path = activityContext.filesDir.absolutePath
        var fileIn = FileInputStream("${path}/${currentUserUid}")
        var imgBytes = fileIn.readBytes()
        var tempBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)
        return tempBitmap.asImageBitmap()
    }

    fun cancelProfileChange()
    {
        userProfilePicture = previousProfilePicture
        //updateEdits()
    }

    fun getDefaultImage(): ImageBitmap
    {
        var img = BitmapFactory.decodeResource(activityContext.resources, R.drawable.ellie)
        return img.asImageBitmap()
    }

    fun getField(field: String): String
    {
        var fieldText = ""
        when(field)
        {
            "username" -> { fieldText = editUserUserame }
            "name" -> { fieldText = editUserRealname }
            "surname" -> { fieldText = editUserSurname }
            "age" -> { fieldText = editUserAge }
            "gender" -> { fieldText = editUserGender }
            "major" -> { fieldText = editUserMajor }
            "school" -> { fieldText = editUserSchool }
            "year" -> { fieldText = editUserYear }
            "bio" -> { fieldText = editUserBio }
        }
        return fieldText
    }

    fun setEdit(field: String, text: String)
    {
        when(field)
        {
            "username" -> { editUserUserame = text }
            "name" -> { editUserRealname = text }
            "surname" -> { editUserSurname = text }
            "age" -> { editUserAge = text }
            "gender" -> { editUserGender = text }
            "major" -> { editUserMajor = text }
            "school" -> { editUserSchool = text }
            "year" -> { editUserYear = text }
            "bio" -> { editUserBio = text }
        }
        updateEdits()
    }

    fun changeActiveWindow(window: Int)
    {
        activeWindow = window
        updateActiveWindow()
    }

    private fun updateActiveWindow()
    {
        _uiState.update { AppUiState(
            activeWindow = activeWindow
        ) }
    }

    private fun updateEdits()
    {
        _uiState.update { AppUiState(
            editUserUserame = editUserUserame,
            editUserRealname = editUserRealname,
            editUserSurname = editUserSurname,
            editUserAge = editUserAge,
            editUserGender = editUserGender,
            editUserSchool = editUserSchool,
            editUserMajor = editUserMajor,
            editUserBio = editUserBio,
            editUserYear = editUserYear,
            userProfilePicture = userProfilePicture,
        ) }
    }

    //  save profile changes
    fun saveEdits()
    {
        var ref = dbObject.reference.child("users").child(currentUserUid).child("Info")

        ref.child("username").setValue(editUserUserame)
        ref.child("name").setValue(editUserRealname)
        ref.child("surname").setValue(editUserSurname)
        ref.child("age").setValue(editUserAge)
        ref.child("gender").setValue(editUserGender)
        ref.child("major").setValue(editUserMajor)
        ref.child("school").setValue(editUserSchool)
        ref.child("quote").setValue(editUserBio)
        ref.child("year").setValue(editUserYear)
    }

    //  retrieves user info at sign-in
    fun getUserInfo()
    {
        var ref = dbObject.reference.child("users").child(currentUserUid).child("Info")
        var publicRef = dbObject.reference.child("users").child(currentUserUid).child("publicInfo")

        ref.child("username").get().addOnSuccessListener{
            currentUserUserame = it.getValue<String>() ?: ""
            editUserUserame = currentUserUserame
            publicRef.child("username").setValue(currentUserUserame)
        }
        ref.child("name").get().addOnSuccessListener{
            currentUserRealname = it.getValue<String>() ?: ""
            editUserRealname = currentUserRealname
        }
        ref.child("surname").get().addOnSuccessListener{
            currentUserSurname = it.getValue<String>() ?: ""
            editUserSurname = currentUserSurname
        }
        ref.child("age").get().addOnSuccessListener{
            currentUserAge = it.getValue<String>() ?: ""
            editUserAge = currentUserAge
        }
        ref.child("gender").get().addOnSuccessListener{
            currentUserGender = it.getValue<String>() ?: ""
            editUserGender = currentUserGender
            ref.child("gender").setValue(currentUserGender)
        }
        ref.child("major").get().addOnSuccessListener{
            currentUserMajor = it.getValue<String>() ?: ""
            editUserMajor = currentUserMajor
        }
        ref.child("school").get().addOnSuccessListener{
            currentUserSchool = it.getValue<String>() ?: ""
            editUserSchool = currentUserSchool
        }
        ref.child("year").get().addOnSuccessListener{
            currentUserYear = it.getValue<String>() ?: ""
            editUserYear = currentUserYear
        }
        ref.child("quote").get().addOnSuccessListener{
            currentUserBio = it.getValue<String>() ?: ""
            editUserBio = currentUserBio
        }
        updateEdits()
    }

    //  set active chat id
    fun setActiveChat(id: String)
    {
        activeChatID = id

        var count = 0
        friendsDetailList.value.friendsDetails.forEach {
            if(it.uid==id)
            {
                activeChatNumber = count
            }
            count+=1
        }

        msgCount = readMap[activeChatID]?.size ?: 0

        updateChatUi()
    }

    fun getMsgs()
    {
        msgCount = readMap[activeChatID]!!.size
        updateChatsUi()
    }

    //  update chat UI
    private fun updateChatUi()
    {
        _uiState.update { AppUiState(
            msgCount = msgCount,
            activeChatID = activeChatID,
            activeChatNumber = activeChatNumber
        ) }
    }

    fun createRef()
    {
        var ref = dbObject.reference.child("zones")

        var key = ref.child("0").push()
        key.setValue("ezNMa82M2FOX0HN4ng2xvVawFC83")
    }

    //  retrieves discovered user info
    fun getDiscoveredInfo()
    {
        //discoveredUserUid = if(userA == currentUserUid){userB}else{userA}
        var ref = dbObject.reference.child("users").child(discoveredUserUid).child("Info")

        //  update fields
        ref.child("username").get().addOnSuccessListener{
            discoveredUserUserame = it.getValue<String>() ?: ""
        }
        ref.child("age").get().addOnSuccessListener{
            discoveredUserAge = it.getValue<String>() ?: ""
        }
        ref.child("bio").get().addOnSuccessListener{
            discoveredUserBio = it.getValue<String>() ?: ""
        }
        ref.child("major").get().addOnSuccessListener{
            discoveredUserMajor = it.getValue<String>() ?: ""
        }
        ref.child("school").get().addOnSuccessListener{
            discoveredUserSchool = it.getValue<String>() ?: ""
        }
        ref.child("year").get().addOnSuccessListener{
            discoveredUserYear = it.getValue<String>() ?: ""
        }
        ref.child("gender").get().addOnSuccessListener{
            discoveredUserGender = it.getValue<String>() ?: ""
        }

        updateUi()

        //getDiscoveredProfile()
    }

    fun getDiscoveredProfile(userID: String, tempUserData: UserClass)
    {
        Log.d("discovered list", "${discoveredList.size}")
        var discoveredID = userID
        //var internalPath = "/data/user/0/com.fishinspace.projectcosmichamster/files"

        //  set reference
        var ref = dbStorage.reference.child("${discoveredID}.jpg")

        val imageBuff = File.createTempFile("userID", ".jpg")
        val ONE_MEGABYTE: Long = 1024 * 1024

        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            Log.d("profile download", "success")
            var img = BitmapFactory.decodeByteArray(it, 0, it.size)
            discoveredImagesList[discoveredID] = img.asImageBitmap()
            //discoveredList.add(tempUserData)
            //updateDiscovered()
            addDiscovered(tempUserData)
            isLoading = false
            //updateDiscoveredImage()
            //swipeOn = true
        }.addOnFailureListener{
            Log.d("profile download", "failed")
            addDiscovered(tempUserData)
            isLoading = false
            //discoveredList.add(tempUserData)
        }
    }

    fun updateDiscoveredImage()
    {
        _uiState.update { AppUiState(
            discoveredImages = discoveredImages
        ) }
    }


    fun resizeImage(image: ByteArray) : ByteArray
    {
        //val btmp = BitmapFactory.decodeByteArray(image, 0, image.size)
        var btmp = BitmapFactory.decodeByteArray(image, 0, image.size)
        Log.d("image dimn", "${btmp.width}x${btmp.height}")
        //val resized = Bitmap.createScaledBitmap(btmp, 200, 200, true)
        val imageBuff = File.createTempFile("tempProfilePicture", ".jpg")
        var outStream = ByteArrayOutputStream()

        //  check if image dimension exceed 2k
        if((btmp.width>2000)||(btmp.height>2000))
        {
            var value = if(btmp.width>btmp.height){btmp.width}else{btmp.height}
            var xM: Double = 2000.0 / value
            var newWidth = xM * btmp.width
            var newHeight = xM * btmp.height
            btmp = btmp.scale(newWidth.toInt(), newHeight.toInt())
        }

        btmp.compress(Bitmap.CompressFormat.JPEG, 10, outStream)
        var bytes = outStream.toByteArray()
        Log.d("image size", "${ outStream.size() }")
        return bytes
    }

    fun uploadProfile(): Int
    {
        if(dbAuth.currentUser==null){return -1}
        var dbStorageRef = dbStorage.reference
        var userId = currentUserUid
        // Create a reference to "mountains.jpg"
        val userProfileRef = dbStorageRef.child("${userId}.jpg")

        // Create a reference to 'profiles/mountains.jpg'
        val profilesRef = dbStorageRef.child("profiles/${userId}.jpg")
        // While the file names are the same, the references point to different files
        userProfileRef.name == profilesRef.name // true
        userProfileRef.path == profilesRef.path

        var streamIn = activityContext.contentResolver.openInputStream(imgUri!!)
        var tempBytes = streamIn!!.readBytes()

        //  shrink image
        var bytes = resizeImage(tempBytes)

        var uploadtask = userProfileRef.putBytes(bytes)
        isUploading = true
        uploadtask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.d("upload", "Failed")
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            Log.d("upload", "Success")
            saveProfileDisk(bytes)
            userProfilePicture = BitmapFactory.decodeByteArray(bytes, 0 , bytes.size).asImageBitmap()
            updateEdits()
            isUploading = false
        }
        return 1
    }

    fun previewProfilePicture()
    {
        try{
            previousProfilePicture = userProfilePicture
            var streamIn = activityContext.contentResolver.openInputStream(imgUri!!)
            var tempBytes = streamIn!!.readBytes()

            //  shrink image
            var bytes = resizeImage(tempBytes)
            userProfilePicture = BitmapFactory.decodeByteArray(bytes, 0 , bytes.size).asImageBitmap()
            Log.d("image dimn", "${ userProfilePicture.width }x${ userProfilePicture.height }")
            updateEdits()
            Log.d("profile preview", "image changed")
        }   catch(e: Exception)
        {
            Log.d("profile preview", e.message.toString())
        }
    }

    //  download profile picture
    fun getProfilePic(friendID: String, userData: UserClass)
    {
        var userID = currentUserUid
        //var internalPath = "/data/user/0/com.fishinspace.projectcosmichamster/files"

        //  set reference
        var ref = dbStorage.reference.child("${friendID}.jpg")

        val imageBuff = File.createTempFile("userID", ".jpg")
        val ONE_MEGABYTE: Long = 1024 * 1024

        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            Log.d("profile download", "success")
            var img = BitmapFactory.decodeByteArray(it, 0, it.size)
            friendProfilePictures[friendID] = img.asImageBitmap()
            addFriend(userData)
            updateUiFriends()
        }.addOnFailureListener{
            Log.d("profile download", "failed")
            updateUiFriends()
        }
    }

    //  download profile picture
    fun getSelfProfilePic()
    {
        var userID = currentUserUid
        //var internalPath = "/data/user/0/com.fishinspace.projectcosmichamster/files"

        //  set reference
        var ref = dbStorage.reference.child("${userID}.jpg")

        val imageBuff = File.createTempFile("userID", ".jpg")
        val ONE_MEGABYTE: Long = 1024 * 1024

        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            Log.d("profile download", "success")
            var img = BitmapFactory.decodeByteArray(it, 0, it.size)
            userProfilePicture = img.asImageBitmap()
            updateEdits()
            saveProfileDisk(it)
        }.addOnFailureListener{
            Log.d("profile download", "failed")
        }
    }

    //  initiate discovery, discovers 5 profiles at startup
    fun exploreUsers()
    {
        //  attach listener to get user ids
        var ref = dbObject.reference.child("users").child(discoveredUserUid)
        //ref.addChildEventListener(exploreListener)
    }

    //  gets next user in discovered pool
    fun getNextDiscovered()
    {
        if(!swipeOn){ return }
        swipeOn = false
        //  hold thread if no users have yet been retrieved
        Executors.newSingleThreadExecutor().execute {
            while(discoveredPool.isEmpty()){}
            discoveredIndex += 1
            if(discoveredIndex==discoveredPool.size){ discoveredIndex = 0 }
            discoveredUserUid = discoveredPool[discoveredIndex]

            getDiscoveredInfo()
            //getDiscoveredProfile()
        }

    }

    //  gets first explore user at sign in
    fun getInitDiscovered()
    {
        //  hold thread if no users have yet been retrieved
        Executors.newSingleThreadExecutor().execute {
            while(discoveredPool.isEmpty()){}
            discoveredUserUid = discoveredPool[discoveredIndex]

            getDiscoveredInfo()
            //getDiscoveredProfile()
        }

    }

    fun attachMJListener(ref: DatabaseReference)
    {
        ref.child("posts").addChildEventListener(postsListener)
    }

    //  signs user in
    @RequiresApi(Build.VERSION_CODES.O)
    fun signIn(email: String, password: String)
    {
        isLoading = true
        dbAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activityThis)
            { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "User Sign In:success")
                    currentUserUid = dbAuth.currentUser!!.uid
                    attachListeners()
                    exploreUsers()

                    //getDiscoveredInfo()
                    //getInitDiscovered()
                    getUserInfo()

                    //  start the fences
                    setupFences()

                    //  start app background service
                    //activityContext.startService(serviceIntent)
                    try{
                        userProfilePicture = getUserImage()
                        updateEdits()
                    }   catch (e: Exception)
                    {
                        Log.d("user profile picture", e.message.toString())
                        getSelfProfilePic()
                    }
                    isLoading = false
                    //sendMessage()
                    navController.navigate(Destination.ExploreScreen.route)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "User Sign In:failure", task.exception)
                    Toast.makeText(activityContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
            }
    }

    //  signUp
    fun signUp(email: String, password: String)
    {
        dbAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activityThis)
            { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    //createUserNodes()
                    currentUserUid = dbAuth.currentUser!!.uid
                    attachListeners()
                    exploreUsers()
                    //getDiscoveredInfo()
                    getInitDiscovered()
                    getUserInfo()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(activityContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    fun updateDiscovered()
    {
        Log.d("discovered list", "${discoveredList.size}")
        _uiState.update { AppUiState(
            discoveredList = discoveredList,
            discoveredImagesList = discoveredImagesList,
        ) }
    }

    fun populateDiscovery(userID: String, key: String)
    {
        //if(userID == currentUserUid){return}

        discoveredDetailList.value.discoveredDetails.forEach {
            if(userID==it.uid)
            {
                return
            }
        }

        var tempMap = mutableMapOf<String, String>()
        var ref = dbObject.reference.child("users").child(userID).child("Info")

        //  grab userUID
        tempMap["uid"] = userID
        tempMap["key"] = key
        Log.d("repeat discovery", "true")
        //  get fields
        ref.child("username").get().addOnSuccessListener{
            tempMap["username"] = it.getValue<String>() ?: ""

            ref.child("age").get().addOnSuccessListener{
                tempMap["age"] = it.getValue<String>() ?: ""

                ref.child("gender").get().addOnSuccessListener{
                    tempMap["gender"] = it.getValue<String>() ?: ""

                    ref.child("quote").get().addOnSuccessListener{
                        tempMap["bio"] = it.getValue<String>() ?: ""

                        ref.child("major").get().addOnSuccessListener{
                            tempMap["major"] = it.getValue<String>() ?: ""

                            ref.child("school").get().addOnSuccessListener{
                                tempMap["school"] = it.getValue<String>() ?: ""

                                ref.child("year").get().addOnSuccessListener {
                                    tempMap["year"] = it.getValue<String>() ?: ""

                                    //  create discovered userid
                                    var tempUserObject = UserClass(
                                        uid = tempMap["uid"].toString(),
                                        key = tempMap["key"].toString(),
                                        age = tempMap["age"].toString(),
                                        quote = tempMap["bio"].toString(),
                                        major = tempMap["major"].toString(),
                                        school = tempMap["school"].toString(),
                                        year = tempMap["year"].toString(),
                                        username = tempMap["username"].toString(),
                                        gender = tempMap["gender"].toString()
                                    )

                                    //discoveredList.add(tempUserObject)
                                    getDiscoveredProfile(userID, tempUserObject)
                                    //updateDiscovered()
                                    //getProfilePic(userID)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun restruct()
    {
        var ref = dbObject.reference.child("users").child(currentUserUid)

        //  create new node
        ref.child("Info").child("name").setValue(currentUserRealname)
        ref.child("Info").child("realname").setValue(null)
    }

    //  use friend id to retrieves their information
    //  should take key and index of where to place in friends list
    fun getFriendDetails(friendID: String, key: String)
    {
        var tempMap = mutableMapOf<String, String>()
        var ref = dbObject.reference.child("users").child(friendID).child("Info")

        //  grab userUID
        tempMap["uid"] = friendID

        //  get fields
        ref.child("username").get().addOnSuccessListener{
            tempMap["username"] = it.getValue<String>() ?: ""

            ref.child("age").get().addOnSuccessListener{
                tempMap["age"] = it.getValue<String>() ?: ""

                ref.child("quote").get().addOnSuccessListener{
                    tempMap["bio"] = it.getValue<String>() ?: ""

                    ref.child("major").get().addOnSuccessListener{
                        tempMap["major"] = it.getValue<String>() ?: ""

                        ref.child("school").get().addOnSuccessListener{
                            tempMap["school"] = it.getValue<String>() ?: ""

                            ref.child("year").get().addOnSuccessListener{
                                tempMap["year"] = it.getValue<String>() ?: ""

                                val tempUserClass = UserClass(uid = tempMap["uid"].toString(), key = key,
                                    age = tempMap["age"].toString(), quote = tempMap["bio"].toString(),
                                    major = tempMap["major"].toString(), school = tempMap["school"].toString(),
                                    year = tempMap["year"].toString(), username = tempMap["username"].toString(),
                                    gender = tempMap["gender"].toString())

                                //friendsDetailList.add(tempMap)
                                //friendsCount = friendsDetailList.size
                                //updateUiFriends()
                                getProfilePic(friendID, tempUserClass)
                            }
                        }
                    }
                }
            }
        }
    }

    //  signs user out
    fun signOut()
    {
        dbAuth.signOut()
        navController.navigate(Destination.SignInScreen.route)
    }

    //  checks if logged in or na
    fun isAuth(): Boolean
    {
        Log.d("log in", dbAuth.currentUser.toString())
        return dbAuth.currentUser!=null
    }

    //  updateUI
    fun updateUi()
    {
        _uiState.update { AppUiState(
            discoveredUserame = discoveredUserUserame,
            discoveredAge = discoveredUserAge,
            discoveredBio = discoveredUserBio,
            discoveredGender = discoveredUserGender,
            discoveredMajor = discoveredUserMajor,
            discoveredName = discoveredUserMajor,
            discoveredSchool = discoveredUserSchool,
            discoveredYear = discoveredUserYear,
        )}
    }

    //  updateUI - Requester
    fun updateUiRequester()
    {
        _uiState.update { AppUiState(
            requesterUsername = requesterUsername,
            requesterAge = requesterAge,
            requesterQuote = requesterQuote,
            requesterGender = requesterGender,
            requesterMajor = requesterMajor,
            requesterSchool = requesterSchool,
            requesterYear = requesterYear,
            requestsInMap = requestsInMap,
            requestInAvailable = requestAvailable,
            requestDetailsList = requestDetailsList,
            requestsInCount = requestsInCount,
            requestImagesMap = requestImagesMap,
        ) }
    }

    //  updateUI - Friends
    fun updateUiFriends()
    {
        Log.d("Xfriends Count", friendsDetailList.value.friendsDetails.size.toString())
        _uiState.update { AppUiState(
            //friendsCount = friendsCount,
            unfriendRecompose = unfriendRecompose,
            //friendsDetailList = friendsDetailList.value!!
        ) }
    }

    //  send request to discovered user
    fun sendRequest(userID: String)
    {
        //  out means those coming from outside
        //  in means those made by current user
        //  if request already made, don't make another
        if(requestsOutMap.containsValue(userID)) return

        var refOut = dbObject.reference.child("users").child(currentUserUid)
        var refIn = dbObject.reference.child("users").child(userID)
        var tempRequests = listOf<String>()

        var requestOutRef = refOut.child("requestsOut").child(userID)
        requestOutRef.setValue(userID)

        var requestInRef = refIn.child("requestsIn").child(currentUserUid)
        requestInRef.setValue(currentUserUid)

        Toast.makeText(activityContext, "request sent", Toast.LENGTH_SHORT).show()
    }

    //  check if request available
    fun checkRequestAvailable()
    {
        requestAvailable = requestsInKeys.isNotEmpty()
        Log.d("requests available", requestAvailable.toString())
        updateUiRequester()
    }

    //  retrieves requester information
    fun getRequester(userUID: String, requesterKey: String)
    {
        if(requestsInMap.isEmpty()){
            requestAvailable=false
            updateUiRequester()
            return
        }
        requestsKnown.add(userUID)
        requestAvailable = true
        Log.d("user uid to add", userUID)
        var targetUser = userUID
        //requesterMapKey = requestsInKeys.first()
        var ref = dbObject.reference.child("users").child(targetUser).child("Info")
        var tempDict = mutableMapOf<String, String>()
        tempDict["key"] = requesterKey
        //  get fields
        ref.child("username").get().addOnSuccessListener{
            requesterUsername = it.getValue<String>() ?: ""
            tempDict["uid"] = userUID
            tempDict["username"] = requesterUsername
            ref.child("age").get().addOnSuccessListener{
                requesterAge = it.getValue<String>() ?: ""
                tempDict["age"] = requesterAge
                ref.child("gender").get().addOnSuccessListener{
                    requesterGender = it.getValue<String>() ?: ""
                    tempDict["gender"] = requesterGender
                    ref.child("major").get().addOnSuccessListener{
                        requesterMajor = it.getValue<String>() ?: ""
                        tempDict["major"] = requesterMajor
                        ref.child("school").get().addOnSuccessListener{
                            requesterSchool = it.getValue<String>() ?: ""
                            tempDict["school"] = requesterSchool
                            ref.child("year").get().addOnSuccessListener{
                                requesterYear = it.getValue<String>() ?: ""
                                tempDict["year"] = requesterYear

                                var tempReqClass = RequesterClass(tempDict["uid"].toString(), tempDict["key"].toString(),
                                    tempDict["username"].toString(), tempDict["age"].toString(),
                                    tempDict["gender"].toString(),tempDict["major"].toString(),
                                    tempDict["school"].toString(),tempDict["year"].toString())

                                Log.d("req details", requestDetailsList.size.toString())
                                getRequesterImage(userUID = userUID, tempReqClass)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getRequesterImage(userUID: String, requesterTempData: RequesterClass)
    {
        var requesterID = userUID
        var userID = currentUserUid
        //  var internalPath = "/data/user/0/com.fishinspace.projectcosmichamster/files"

        //  set reference
        var ref = dbStorage.reference.child("${requesterID}.jpg")

        //
        val imageBuff = File.createTempFile("userID", ".jpg")
        val ONE_MEGABYTE: Long = 1024 * 1024

        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            Log.d("profile download", "success")
            var img = BitmapFactory.decodeByteArray(it, 0, it.size)
            requestImagesMap[requesterID] = img.asImageBitmap()
            requestsInCount = requestDetailsList.size
            if(requestsInCount>0){ requestAvailable = true}
            Log.d("requests available", requestAvailable.toString())
            Log.d("requests available count", requestsInCount.toString())
            updateDiscoveredImage()
            //requestDetailsList.add(requesterTempData)
            addRequestIn(requesterTempData)
        }.addOnFailureListener{
            Log.d("profile download", "failed")
            addRequestIn(requesterTempData)
        }
    }

    fun updateRequesterImage()
    {
        _uiState.update { AppUiState(
            requesterImage = requesterImage
        ) }
    }

    //  attach listeners
    fun attachListeners()
    {
        var ref = dbObject.reference.child("users").child(currentUserUid)
        var zoneRef = dbObject.reference.child("zones")

        //  set listeners
        ref.child("requestsOut").addChildEventListener(requestsOutListener)
        ref.child("requestsIn").addChildEventListener(requestsInListener)
        ref.child("requestsRejected").addChildEventListener(requestsRejectedListener)
        ref.child("friends").addChildEventListener(friendsListener)
        ref.child("unfriend").addChildEventListener(unfriendListener)
        ref.child("chats").child("new messages").addChildEventListener(newMessagesListener)
        //zoneRef.child("0").addChildEventListener(discoveredListener)
    }
    var mainRef = dbObject.reference.child("users").child(currentUserUid)
    fun attachMessageListeners(id: String)
    {
        var ref = dbObject.reference.child("users").child(currentUserUid)

        try{
            ref.child("chats").child(id).child("unread").addChildEventListener(unreadListener)
            //mainRef = ref.child("chats").child(id).child("unread")
            ref.child("chats").child(id).child("read").addChildEventListener(readListener)
            //cntx.startService(svc)
        } catch (e: Exception){
            Log.d("error service", e.message.toString())
            ref.child("chats").child("new messages").addChildEventListener(newMessagesListener)
        }
    }

    //  clear unread messages once chat has been opened
    fun clearUnread(userID: String)
    {
        var ref = dbObject.reference.child("users").child(currentUserUid).child("chats")
            .child(userID)

        var userMessages: MutableList<MessageClass>?
        try{
            userMessages = messagesUnreadMap.value.messagesUnreadDetails[userID]
            if(userMessages==null){return}
        }   catch (e:Exception)
        {
            return
        }

        if(userMessages.isEmpty()){ return }
        var count = 0
        //  push all unread messages to read
        userMessages.forEach {
            var item = it
            var tempMap = mapOf<String, String>("id" to userID, "msg" to it.message, "sender" to it.sender
                ,"post" to it.post)
            ref.child("read").child(it.date).setValue(tempMap).addOnSuccessListener {
                userMessages.remove(item)
            }
        }

        //  clear all unread messages
        ref.child("unread").setValue(null)
    }

    //  decline request, is removed if so
    fun declineRequest(userUID: String)
    {
        if(requestsInMap.isEmpty()){
            requestAvailable=false
            updateUiRequester()
            return
        }

        requestsInMap.forEach {
            if(it.value==userUID)
            {
                requesterMapKey = it.key
            }
        }

        Log.d("Declined UID", userUID)

        var reqUser = userUID
        var rejectedRefs = dbObject.reference.child("users").child(reqUser).child("requestsRejected")
        rejectedRefs.child(currentUserUid).setValue(currentUserUid)

        Log.d("requester map key", requesterMapKey)
        var requestInref = dbObject.reference.child("users").child(currentUserUid).child("requestsIn")
        requestInref.child(requesterMapKey).removeValue()

        requestsKnown.remove(userUID)
        requestsInMap.remove(requesterMapKey)
        requestsInKeys.remove(requesterMapKey)
    }

    fun requestsCount()
    {
        requestsInCount = requestsInMap.size
        if(requestsInCount>0){ requestAvailable = true}
        updateUiRequester()
    }

    fun friendsCount()
    {
        friendsCount = friendsDetailList.value.friendsDetails.size
        updateUiFriends()
    }

    //  update chats ui
    fun updateChatsUi()
    {
        _uiState.update { AppUiState(
            messagesMap = unreadMap
        ) }
    }

    fun sendMessage(message: String)
    {
        val time = System.currentTimeMillis().toString()

        var tempMap = mapOf("sender" to "to", "msg" to message, "id" to activeChatID, "post" to "none")

        dbObject.reference.child("users").child(currentUserUid).child("chats").child(activeChatID)
            .child("read").child(time).setValue(tempMap)

        tempMap = mapOf("sender" to "from", "msg" to message, "id" to currentUserUid, "post" to "none")
        dbObject.reference.child("users").child(activeChatID).child("chats")
            .child("new messages").child(currentUserUid).child(time).setValue(tempMap)

    }

    //  accept request
    fun acceptRequest(userUID: String, requesterKey: String)
    {
        if(requestsInMap.isEmpty()){
            requestAvailable=false
            updateUiRequester()
            return
        }

        //  add friend to current user list
        var requesterId = userUID
        var ref = dbObject.reference.child("users").child(currentUserUid).child("friends")
        ref.child(requesterId).setValue(requesterId)

        //  remove request in since it has been approved
        ref = dbObject.reference.child("users").child(currentUserUid).child("requestsIn")
        ref.child(requesterKey).setValue(null)

        //  also add current user as friend to requester list
        ref = dbObject.reference.child("users").child(requesterId).child("friends")
        ref.child(currentUserUid).setValue(currentUserUid)

        //  flag to clear request
        var rejectedRefs = dbObject.reference.child("users").child(requesterId).child("requestsRejected")
        rejectedRefs.child(currentUserUid).setValue(currentUserUid)

        //  pop user from requests list
        requestsInMap.remove(requesterKey)
        requestsInKeys.remove(requesterKey)
        requestsKnown.remove(userUID)
    }

    //  unfriend
    fun unfriend(userID: String, userKey: String)
    {
        if(friendsMap.isEmpty()){
            return
        }

        //  remove friend from current user list
        var ref = dbObject.reference.child("users").child(currentUserUid).child("friends")
        ref.child(userKey).setValue(null)

        //  also remove current user as friend from friend account by adding out userid to their unfriended list
        ref = dbObject.reference.child("users").child(userID).child("unfriend").child(currentUserUid)
        ref.setValue(currentUserUid)
    }

    //  unfriend
    fun clearUnfriend(userID: String, unfriendKey: String)
    {
        if(friendsDetailList.value.friendsDetails.isEmpty()){
            return
        }

        //  key to id
        var key = ""
        var ref = dbObject.reference.child("users").child(currentUserUid)

        //  remove ids from friends list that have unfriended
        var toRemove = 0
        var count = 0

        //  get user key
        var temp = friendsDetailList.value.friendsDetails
        temp.forEach {
            if(userID==it.uid)
            {
                key = it.key
                toRemove = count
            }
            count+=1
        }

        //remove user from database ** friends and unfriend lists
        ref.child("friends").child(key).setValue(null)
        ref.child("unfriend").child(unfriendKey).setValue(null)
    }

    //  clear rejected requests
    fun clearRejected(userID: String, rejectedKey: String)
    {
        var ref = dbObject.reference.child("users").child(currentUserUid)

        //  remove user from requests out
        var tempKey = ""
        requestsOutMap.forEach {
            if(userID==it.value)
            {
                tempKey = it.key
            }
        }

        ref.child("requestsOut").child(tempKey).setValue(null).addOnSuccessListener {
            ref.child("requestsRejected").child(rejectedKey).setValue(null)
            requestsOutMap.remove(tempKey)
            requestsOutKeys.remove(tempKey)
        }

    }

    //  if unfriend received
    fun onUnfriend(uid: String)
    {
        var temp = friendsDetailList.value.friendsDetails
        var toRemove = 0
        var count = 0
        var isFound = false

        temp.forEach {
            if(it.uid==uid)
            {
                isFound = true
                toRemove = count
            }
            count+=1
        }

        if(!isFound){return}
        temp.removeAt(toRemove)
        friendsCount = temp.size
        _friendsDetailList.update { FriendsClass(friendsDetails = temp, friendsCount = friendsCount) }
    }

    fun saveProfileDisk(data: ByteArray)
    {
        var path = activityContext.filesDir.absolutePath
        var outFile = FileOutputStream("${ path }/profile.jpg")
        outFile.write(data)
        Log.d("path", path)
    }

    //  new zone implementation
    //  create minor zone, takes: id, coords, location e.g ekici,
    //  type: Major, parent zone/ major zone/ parent id, it's in
    fun createMinorZone(zoneObj: MinorZoneClass)
    {
        var type = zoneObj.type
        var id = zoneObj.id
        var xcoords = zoneObj.lat.toString().replace(".", "*")
        var ycoords = zoneObj.lng.toString().replace(".", "*")
        var location = zoneObj.location
        var majorID = zoneObj.majorID

        var ref = dbObject.reference.child("zones").child(majorID).child("minors").child(id)

        ref.child("id").setValue("$type*$id")
        ref.child("location").setValue(location)
        ref.child("coords").child("latitude").setValue(xcoords)
        ref.child("coords").child("longitude").setValue(ycoords)
        ref.child("type").setValue(type)
    }

    //  create major zone, takes: id, coords, location e.g lefke,
    //  type: Major
    fun createMajorZone(zoneObj: MajorZoneClass)
    {
        var type = zoneObj.type
        var id = zoneObj.id
        var xcoords = zoneObj.lat.toString().replace(".", "*")
        var ycoords = zoneObj.lng.toString().replace(".", "*")
        var location = zoneObj.location

        var ref = dbObject.reference.child("zones").child(id)

        ref.child("id").setValue("$type*$id")
        ref.child("location").setValue(location)
        ref.child("coords").child("latitude").setValue(xcoords)
        ref.child("coords").child("longitude").setValue(ycoords)
        ref.child("type").setValue(type)
    }

    fun saveZones()
    {
        //  upload major zone
        var zoneObjMN = MajorZoneClass(
            id = "0",
            type = "mj",
            lat = 35.12563,
            lng = 32.84009,
            radius = 500f,
            location = "lefke",
        )

        createMajorZone(zoneObjMN)

        //  upload minor zone
        var zoneObjMJ = MinorZoneClass(
            id = "0",
            type = "mn",
            lat = 35.12547,
            lng = 32.83916,
            radius = 100f,
            location = "ekici",
            majorID = "0",
        )

        createMinorZone(zoneObjMJ)
    }

    //  posts messages to major zone board, given you're currently in one
    fun postToBoard(cat: String, specifier: String, note: String)
    {
        //  if not in a major zone, return
        if(currentMajorZone==""){return}

        var timeStamp = System.currentTimeMillis()
        var zoneID = currentMajorZone.split("*")
        var ref = dbObject.reference.child("zones").child(zoneID[1])

        //  run a transaction
        ref.runTransaction(object: Transaction.Handler{
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                currentData.child("posts").child(currentUserUid).child("category").value = cat
                currentData.child("posts").child(currentUserUid).child("specifier").value =
                    specifier
                currentData.child("posts").child(currentUserUid).child("note").value = note
                currentData.child("posts").child(currentUserUid).child("time").value = timeStamp

                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {

                if(committed){
                    Toast.makeText(activityContext, "Post added to board.", Toast.LENGTH_SHORT).show()
                    Log.d("Post To Board", "Post has been added to board")
                } else{
                    Toast.makeText(activityContext, "Failed to Post.", Toast.LENGTH_SHORT).show()
                    Log.d("Post To Board", "Failed to post to board")
                }
            }
        })

    }

    //  reset open chat id and message count when user closes chat
    fun resetOpenChat()
    {
        openChatMsgCount = 0
        _openChat.update { OpenChatClass(chatID = "none", messageCount = 0) }
    }

    fun sendVerificationEmail()
    {
        auth.currentUser!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("verification", "Email sent.")
                }
            }
    }

    fun getLastMessage(userID: String): MessageClass
    {
        if(messagesUnreadMap.value.messagesUnreadDetails.containsKey(userID)) {
            return messagesUnreadMap.value.messagesUnreadDetails[userID]!!.last()
        }
        return messagesMap.value.messagesDetails[userID]!!.last()
    }

    fun setHighlight(userID: String, message: MessageClass)
    {
        var temp = _messageHighlights.value.messageHighlightsDetails
        temp[userID] = message
        var count = _messageHighlights.value.messageHighlightsCount+1
        _messageHighlights.update { MessageHighlightsClass(messageHighlightsDetails = temp, messageHighlightsCount = count) }
    }

    fun replyToPost(userID: String, heading: String, note: String, message: String)
    {
        var ref = dbObject.reference.child("users")
        var date = System.currentTimeMillis().toString()

        //  Save to user read messages
        var tempData = mutableMapOf("sender" to "to", "date" to date, "id" to userID,
            "msg" to message, "post" to "${heading}*~*${note}"
        )

        ref.child(currentUserUid).child("chats").child(userID).child("read")
            .child(date).setValue(tempData)

        //  Send to recipient
        tempData = mutableMapOf("sender" to "from", "date" to date, "id" to currentUserUid,
            "msg" to message, "post" to "${heading}*~*${note}"
        )

        ref.child(userID).child("chats").child("new messages").child(currentUserUid)
            .child(date).setValue(tempData)
    }

    //  Presets the category, specifier, and note
    fun preparePostEdit(category: String, specifier: String, note: String)
    {
        postEditCategory = category
        postEditSpecifier = specifier
        postEditNote = note
    }

    //  Deletes posts from board
    //  Should take a id number for the zone,
    fun deletePost()
    {
        //  get current major zone id
        var zoneID = currentMajorZone.split("*")[1]
        var ref = dbObject.reference.child("zones").child(zoneID)
        ref.child("posts").child(currentUserUid).setValue(null)
    }

    //  Removes post from list if removed from database
    fun removePost(userID: String)
    {
        var tempList = _postsList.value.postsList

        var postIndex = tempList.find {
            it.userID == userID
        }

        if(postIndex!=null)
        {
            tempList.remove(postIndex)
            postsCount = tempList.size
            _postsList.update { PostsInClass(postsList = tempList, postsCount = postsCount) }
            Toast.makeText(activityContext, "Post deleted!", Toast.LENGTH_SHORT)
        }
    }

    fun resetPassword(email: String)
    {
        dbAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            Toast.makeText(activityContext ,"Reset email sent.", Toast.LENGTH_SHORT).show()
        }
    }

    /*//  retrieves requests for current user
    @RequiresApi(Build.VERSION_CODES.O)
    fun getRequests()
    {
        var ref = dbObject.reference.child("users").child(currentUserUid)

        ref.child("requests").get().addOnSuccessListener{
            var ss = it.getValue<Any>() ?: ""
            Log.d("request response", ss::class.java.typeName)
        }
    }*/

}