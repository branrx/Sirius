package com.fishinspace.projectcosmichamster

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fishinspace.projectcosmichamster.ui.AboutScreen
import com.fishinspace.projectcosmichamster.ui.AppViewModel
import com.fishinspace.projectcosmichamster.ui.ChatScreen
import com.fishinspace.projectcosmichamster.ui.CreatePostScreen
import com.fishinspace.projectcosmichamster.ui.EditPostScreen
import com.fishinspace.projectcosmichamster.ui.FillProfileScreen
import com.fishinspace.projectcosmichamster.ui.HomeScreen
import com.fishinspace.projectcosmichamster.ui.MessagesScreen
import com.fishinspace.projectcosmichamster.ui.PasswordResetScreen
import com.fishinspace.projectcosmichamster.ui.PermissionsScreen
import com.fishinspace.projectcosmichamster.ui.PostsScreen
import com.fishinspace.projectcosmichamster.ui.ProfileScreen
import com.fishinspace.projectcosmichamster.ui.RequestsScreen
import com.fishinspace.projectcosmichamster.ui.SignInScreen
import com.fishinspace.projectcosmichamster.ui.SignUpScreen
import com.fishinspace.projectcosmichamster.ui.defaultImage
import com.fishinspace.projectcosmichamster.ui.getAllDefaults
import com.fishinspace.projectcosmichamster.ui.getDefaultImage
import com.fishinspace.projectcosmichamster.ui.initPermissions
import com.fishinspace.projectcosmichamster.ui.serviceIntent
import com.fishinspace.projectcosmichamster.ui.theme.ProjectCosmicHamsterTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


//  Useful variables
lateinit var auth: FirebaseAuth
lateinit var activityThis: ComponentActivity
lateinit var navController: NavHostController
lateinit var appViewModel: AppViewModel

//  Stores current location object
var currentLocation: Location? = null

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)

    override fun onPause() {
        super.onPause()
        //removeFromZone()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {
        super.onBackPressed()

        //onBackPressedDispatcher.addCallback(onBackPressedCallback = OnBackPressedCallback.)
        Log.d("back pressed", "back pressed")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.database.setPersistenceEnabled(true)
        setContent {
            ProjectCosmicHamsterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var ac = LocalView.current.context as Activity
                    ac.window.statusBarColor = if(isSystemInDarkTheme())
                    {
                        MaterialTheme.colorScheme.surface.toArgb()
                    }   else{
                        MaterialTheme.colorScheme.onSurface.toArgb()
                    }

                    //WindowCompat.setDecorFitsSystemWindows(window, false)
                    //  Initiate ads
                    //MobileAds.initialize(this)

                    //  I constantly need the context and activity
                    //  Used later on, e.g for triggering toasts, broadcasts etc
                    activityThis = this

                    //  Initiate ViewModel
                    getAllDefaults(LocalContext.current)
                    appViewModel = viewModel()


                    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

                    //  Bind Location receiver to
                    //  Settings: intervals, priority etc..
                    //  Handler: what to do with received object if location is updated
                    //  Looper: handles the threading so operation is not done on main thread
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                    }
                    fusedLocationProviderClient.getCurrentLocation(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                    //fusedLocationProviderClient.lastLocation
                    //  Create foreground service intent
                    //  Handles: messages, discoveries while app is in background
                    serviceIntent = Intent(this, ChatBackgroundService::class.java)

                    //  Create notification channels for discovery and messages
                    createNotificationChannel("1", "new discovery", LocalContext.current)
                    createNotificationChannel("2", "new message", LocalContext.current)

                    //  Check permissions
                    var permissions = initPermissions(LocalContext.current)

                    //  Upload fence
                    //removeGeofence()
                    //uploadMajorZone(fencesTempMJ)
                    //uploadMinorZone(fencesTempMN)

                    //  if permissions are allowed go to sign in else permissions screen
                    var rootScreen = if(permissions){"signin_screen"}else{"permissions_screen"}
                    //rootScreen = "explore_screen"

                    //  Initiate navigation framework
                    rootScreen = "signin_screen"
                    navController = rememberNavController()
                    navController.enableOnBackPressed(true)
                    NavigationAppHost(navController, rootScreen)

                }
            }
        }
    }

    fun notifier(builder: Notification, context: Context)
    {
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    context,
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

    fun createNotificationChannel(id: String, channelName: String, context: Context)
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
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun buildNotification(user: String, msg: String, id: String, context: Context): Notification
    {
        val builder = NotificationCompat.Builder(context, id)
            .setSmallIcon(R.drawable.cirqle_logo_3)
            .setContentTitle(user)
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setAutoCancel(true)
        return builder.build()
    }
}

//  Screen destination routes
sealed class Destination(val route: String)
{
    object SignInScreen: Destination("signin_screen")
    object PasswordResetScreen: Destination("passwordreset_screen")
    object SignUpScreen: Destination("signup_screen")
    object PermissionsScreen: Destination("permissions_screen")
    object FillProfileScreen: Destination("fillprofile_screen")
    object ExploreScreen: Destination("explore_screen")
    object PostScreen: Destination("post_screen")
    object CreatePostScreen: Destination("createpost_screen")
    object EditPostScreen: Destination("editpost_screen")
    object MessagesScreen: Destination("messages_screen")
    object RequestsScreen: Destination("requests_screen")
    object ChatScreen: Destination("chats_screen")
    object ProfileScreen: Destination("profile_screen")
    object AboutScreen: Destination("about_screen")
}

//  Navigation frame, responsible for traversing through different
//  screens in the app
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationAppHost(navController: NavHostController, rootScreen: String)
{
    NavHost(navController = navController, startDestination = rootScreen, enterTransition = {
      fadeIn(animationSpec = tween(250, 0, easing = LinearEasing))
    }, exitTransition = {
        fadeOut(animationSpec = tween(250, 0, easing = LinearEasing))
    }
    )
    {
        composable(Destination.SignInScreen.route) { SignInScreen() }
        composable(Destination.PasswordResetScreen.route) { PasswordResetScreen() }
        composable(Destination.SignUpScreen.route) { SignUpScreen() }
        composable(Destination.PermissionsScreen.route) { PermissionsScreen() }
        composable(Destination.FillProfileScreen.route) { FillProfileScreen() }
        composable(Destination.ExploreScreen.route) { HomeScreen() }
        composable(Destination.PostScreen.route) { PostsScreen() }
        composable(Destination.CreatePostScreen.route) { CreatePostScreen() }
        composable(Destination.EditPostScreen.route) { EditPostScreen() }
        composable(Destination.RequestsScreen.route) { RequestsScreen() }
        composable(Destination.MessagesScreen.route) { MessagesScreen() }
        composable(Destination.ChatScreen.route) { ChatScreen() }
        composable(Destination.AboutScreen.route) { AboutScreen() }
        composable(Destination.ProfileScreen.route) { ProfileScreen() }
    }
}

@SuppressLint("StaticFieldLeak")
var contextWrapper = ContextWrapper()
class ContextWrapper()
{
    var contextWrapper: Context?=null;
}

