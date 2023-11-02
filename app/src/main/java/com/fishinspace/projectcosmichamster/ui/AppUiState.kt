package com.fishinspace.projectcosmichamster.ui

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.activityContext

//  Holds ui state variables
data class AppUiState(
    var isConnected: Boolean = false,
    var discoveredUserame: String = "",
    var discoveredAge: String = "",
    var discoveredBio: String = "",
    var discoveredName: String = "",
    var discoveredGender: String = "",
    var discoveredYear: String = "",
    var discoveredMajor: String = "",
    var discoveredSchool: String = "",
    var requesterUsername: String = "",
    var requesterAge: String = "",
    var requesterQuote: String = "",
    var requesterMajor: String = "",
    var requesterSchool: String = "",
    var requesterYear: String = "",
    var requesterGender: String = "",
    var requestsInMap: MutableMap<String, String> = mutableMapOf(),
    var requestInAvailable: Boolean = false,
    var requestsOutMap: MutableMap<String, String> = mutableMapOf(),
    var friendsDetailList: MutableList<UserClass> = mutableListOf(),
    var unfriendRecompose: Boolean = false,
    var activeChatID: String = "",
    var messagesMap: MutableMap<String, MutableList<MutableMap<String, String>>> = mutableMapOf(),
    var msgCount: Int = 0,
    var activeChatNumber: Int = 0,
    var editUserUserame: String = "",
    var editUserRealname: String = "",
    var editUserSurname: String = "",
    var editUserAge: String = "",
    var editUserGender: String = "",
    var editUserSchool: String = "",
    var editUserMajor: String = "",
    var editUserBio: String = "",
    var editUserYear: String = "",
    var activeWindow: Int = 1,
    var discoveredImages: ImageBitmap = getDefaultImage(),
    var requesterImage: ImageBitmap = getDefaultImage(),
    var userProfilePicture: ImageBitmap = getDefaultImage(),
    var requestDetailsList :MutableList<RequesterClass> = mutableListOf(),
    var requestImagesMap: MutableMap<String, ImageBitmap> = mutableMapOf(),
    var requestsInCount: Int = 0,
    var friendsCount: Int = 0,
    var geoFenceStatus: String = "none",
    var discoveredList: MutableList<UserClass> = mutableListOf(),
    var discoveredImagesList: MutableMap<String, ImageBitmap> = mutableMapOf(),
    var xfriendsCount: Int = 0,
)


//  If a person sends you a request,
//  this class holds their information, name, age etc
data class RequesterClass(val uid: String,
                          val key: String,
                val username: String,
                val age: String,
                val gender: String,
                val major: String,
                val school: String,
                val year: String,
)

//  This class holds the user's information, name, age etc
data class UserClass(val uid: String,
                     val key: String,
                     val username: String,
                     val age: String,
                     val gender: String,
                     val major: String,
                     val school: String,
                     val year: String,
                     val quote: String,
)

//  Message object holds the message, date, and type, recipient
data class MessageClass(val sender: String,
                        val message: String,
                        val date: String,
                        val post: String,
)

//  Class holds messages that have been read or opened
data class MessageReadClass(var messagesDetails: MutableMap<String, MutableList<MessageClass>> = mutableMapOf()
)

//  Class holds information on which chat is open
data class OpenChatClass(var chatID: String = "none",
                         var messageCount: Int = 0
)

//  Class holds messages that have not yet been read or opened
data class MessageUnreadClass(var messagesUnreadDetails: MutableMap<String, MutableList<MessageClass>> = mutableMapOf()
)

//  Class holding all the users friends and count
data class FriendsClass(var friendsDetails: MutableList<UserClass> = mutableListOf(),
                        var friendsCount: Int = 0
)

//  Class holds messages that are to be used on the main messages screen
//  These are the last messages sent or received per chat
data class MessageHighlightsClass(var messageHighlightsDetails: MutableMap<String, MessageClass> = mutableMapOf(),
                                  var messageHighlightsCount: Int = 0
)

//  Class holds all the users that have been discovered and count
data class DiscoveredClass(var discoveredDetails: MutableList<UserClass> = mutableListOf(),
                        var discoveredCount: Int = 0
)

//  Class holds all requests received
data class RequestsInClass(var requestsInDetails: MutableList<RequesterClass> = mutableListOf(),
                           var requestsInCount: Int = 0
)

//  Class holds all minor zones defined in the database
data class MinorZoneClass(var id: String,
                          var type: String,
                          var lat: Double,
                          var lng: Double,
                          var radius: Float,
                          var location: String,
                          var majorID: String,
)

//  Class holds all major zones defined in the database
data class MajorZoneClass(var id: String,
                          var type: String,
                          var lat: Double,
                          var lng: Double,
                          var radius: Float,
                          var location: String,
)

//  Class holds all posts in the board
data class PostsInClass(var postsList: MutableList<PostClass> = mutableListOf(),
                        var postsCount: Int = 0
)

//  Class holds information of a post, sender, category, time, note etc
data class PostClass(var userID: String,
                     var username: String,
                     var gender: String,
                     var note: String,
                     var time: Long,
                     var cat: String,
                     var specifier: String,
                     )

//  A message can either be a normal message or a reply to a post
enum class MessageType {
    NORMAL, POST_REPLY
}

//  Password entry can be reentry or just a normal password
enum class PasswordType {
    NORMAL, RE_ENTRY
}

fun getDefaultImage(): ImageBitmap
{
    var path = "/sdcard/Pictures/ellie.jpg"
    var img = BitmapFactory.decodeResource(activityContext.resources, R.drawable.ellie)
    return img.asImageBitmap()
}