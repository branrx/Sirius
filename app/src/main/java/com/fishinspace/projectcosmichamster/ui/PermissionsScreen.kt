package com.fishinspace.projectcosmichamster.ui

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.navController
import kotlinx.coroutines.delay
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PermissionsScreen()
{
    //  If SDK lower than 33 use READ_EXTERNAL_STORAGE else ...
    rwPermission = if(Build.VERSION.SDK_INT>=33)
    {
        Manifest.permission.READ_MEDIA_IMAGES
    }   else{
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    //  Current Page: Which permission is being requested
    var currentPage by remember { mutableIntStateOf(0) }

    //  Permission icon
    var permissionIcon by remember { mutableIntStateOf(R.drawable.point_on_map_svgrepo_com) }

    //  : Assigns the permission is being requested based on current page
    var permissionChk by remember { mutableStateOf("") }

    when(currentPage)
    {
        0 -> { permissionChk = Manifest.permission.ACCESS_FINE_LOCATION; permissionIcon = R.drawable.point_on_map_svgrepo_com }
        1 -> { permissionChk = rwPermission; permissionIcon = R.drawable.camera_svgrepo_com__2_ }
        2 -> { permissionChk = Manifest.permission.POST_NOTIFICATIONS; permissionIcon = R.drawable.notification_unread_lines_svgrepo_com }
        3 -> { permissionChk = Manifest.permission.ACCESS_BACKGROUND_LOCATION; permissionIcon = R.drawable.gps_svgrepo_com }
    }

    var isGrantedGlobal by remember{ mutableStateOf(false) }

    //  Check if permission is granted
    isGrantedGlobal = checkPermission(permissionChk)

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult = {
            isGranted -> isGrantedGlobal = isGranted
    })

    //  Waits till user grants permission then triggers next screen
    LaunchedEffect(key1 = Unit, block =
    {
        while (!isGrantedGlobal)
        {
            isGrantedGlobal = checkPermission(permissionChk)
            Log.d("Permission Debugging", "${permissionChk},${isGrantedGlobal},${currentPage}")
            delay(200)
        }
    })

    Row {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(16.dp)
                .weight(0.4f)
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            )
            {
                Icon(
                    painter = painterResource(id = R.drawable.cirqle_logo_3),
                    contentDescription = "app icon",
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(24.dp)
                        .clipToBounds(),
                )
                Text(text = stringResource(id = R.string.app_name), modifier = Modifier
                    .padding(top = 12.dp), fontFamily = bison,
                    fontWeight = FontWeight.Bold, letterSpacing = 1.sp, fontSize = 40.sp)
                Text(text = "Sign-In", modifier = Modifier.alpha(0f), fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 24.sp)
            }

            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 32.dp, end = 32.dp, top = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                //  Image of a fish, company theme
                Icon(
                    painter = painterResource(id = permissionIcon),
                    contentDescription = "permission icon",
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, top = 0.dp)
                        .weight(0.2f),
                )
                Text(
                    text = pages[currentPage].toString(), fontFamily = bison,
                    letterSpacing = 2.sp, fontSize = 24.sp, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .weight(0.3f)
                        //.align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp)
                )
                Text(
                    text = pageDescriptions[currentPage].toString(), fontFamily = yanone,
                    letterSpacing = 2.sp, fontSize = 20.sp, fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .weight(0.4f)
                        .alpha(0.7f)
                        .padding(top = 0.dp), textAlign = TextAlign.Center
                )
            }
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.weight(0.2f))
            {
                if (!isGrantedGlobal) {
                    ElevatedButton(
                        onClick = {
                            var permission = ""
                            when(currentPage)
                            {
                                0 -> { permission = Manifest.permission.ACCESS_FINE_LOCATION }
                                1 -> { permission = rwPermission }
                                2 -> { permission = Manifest.permission.POST_NOTIFICATIONS }
                                3 -> { permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION }
                            }
                            Log.d("permission name", permission)
                            Executors.newSingleThreadExecutor().execute {
                                launcher.launch(
                                    permission
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxHeight(0.35f)
                            .fillMaxWidth(0.7f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(1f)
                        ) {
                            Text(text = "Allow ${pages[currentPage].toString()}")
                        }
                    }
                } else {
                    ElevatedButton(
                        onClick = {
                                  currentPage+=1
                            if(Build.VERSION.SDK_INT<29 && currentPage > 2)
                            {
                                currentPage-=1
                                navController.navigate(Destination.SignInScreen.route)
                            }   else if(currentPage>3)
                            {
                                currentPage = 3
                                navController.navigate(Destination.SignInScreen.route)
                            }

                        },
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .padding(bottom = 32.dp)
                            .fillMaxHeight(0.35f)
                            .fillMaxWidth(0.7f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(0.2f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_left_svgrepo_com),
                                contentDescription = "proceed arrow",
                                modifier = Modifier
                                    .scale(0.9f)
                                    .rotate(180f)
                            )
                        }
                    }
                }
            }
        }
    }
}
