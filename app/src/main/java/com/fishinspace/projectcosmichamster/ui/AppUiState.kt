package com.fishinspace.projectcosmichamster.ui

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.activityContext

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

data class RequesterClass(val uid: String,
                          val key: String,
                val username: String,
                val age: String,
                val gender: String,
                val major: String,
                val school: String,
                val year: String,
)

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

data class MessageClass(val sender: String,
                        val message: String,
                        val date: String,
                        val post: String,
)

data class MessageReadClass(var messagesDetails: MutableMap<String, MutableList<MessageClass>> = mutableMapOf()
)

data class OpenChatClass(var chatID: String = "none",
                         var messageCount: Int = 0
)

data class MessageUnreadClass(var messagesUnreadDetails: MutableMap<String, MutableList<MessageClass>> = mutableMapOf()
)

data class FriendsClass(var friendsDetails: MutableList<UserClass> = mutableListOf(),
                        var friendsCount: Int = 0
)

data class MessageHighlightsClass(var messageHighlightsDetails: MutableMap<String, MessageClass> = mutableMapOf(),
                                  var messageHighlightsCount: Int = 0
)

data class DiscoveredClass(var discoveredDetails: MutableList<UserClass> = mutableListOf(),
                        var discoveredCount: Int = 0
)

data class RequestsInClass(var requestsInDetails: MutableList<RequesterClass> = mutableListOf(),
                           var requestsInCount: Int = 0
)

data class MinorZoneClass(var id: String,
                          var type: String,
                          var lat: Double,
                          var lng: Double,
                          var radius: Float,
                          var location: String,
                          var majorID: String,
)

data class MajorZoneClass(var id: String,
                          var type: String,
                          var lat: Double,
                          var lng: Double,
                          var radius: Float,
                          var location: String,
)

data class PostsInClass(var postsList: MutableList<PostClass> = mutableListOf(),
                        var postsCount: Int = 0
)

data class PostClass(var userID: String,
                     var username: String,
                     var gender: String,
                     var note: String,
                     var time: Long,
                     var cat: String,
                     var specifier: String,
                     )

enum class MessageType {
    NORMAL, POST_REPLY
}

fun getDefaultImage(): ImageBitmap
{
    var path = "/sdcard/Pictures/ellie.jpg"
    var img = BitmapFactory.decodeResource(activityContext.resources, R.drawable.ellie)
    return img.asImageBitmap()
}