package com.fishinspace.projectcosmichamster.ui

import android.util.Log
import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController

@Composable
fun RequestsScreen()
{
    //  Holds all requests received from server
    //  if new request is received, UI recomposes
    val listA by appViewModel.requestsInDetailList.collectAsState()
    val screenheight = LocalConfiguration.current.screenHeightDp
    val showMainMenu by appViewModel.uiState.collectAsState()
    Log.d("sampled -- Request", listA.requestsInDetails.size.toString())

    //  Entire screen
    Box()
    {
        Column(modifier = Modifier.fillMaxSize())
        {
            //  Top bar, holds icon and other buttons
            Row(modifier = Modifier
                .height((0.07f * screenheight).dp)
                .fillMaxWidth())
            {
                topBarComposable()
            }

            Row(modifier = Modifier
                .requiredHeight(40.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            )
            {
                Surface(modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .padding(8.dp), shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.inversePrimary)))
                )
                {
                    Row(modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center)
                    {
                        Text(text = "REQUESTS", fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = 1.sp)
                    }
                }
            }

            //  Main card wrapper
            Card(modifier = Modifier
                .weight(0.9f)
                .padding(8.dp)
                .shadow(0.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            )
            {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                )
                {
                    //  Get requests count, if available display them
                    val requestsAvailable = listA.requestsInCount>0
                    AnimatedVisibility(requestsAvailable)
                    {
                        LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = Modifier.fillMaxSize())
                        {
                            items(items = listA.requestsInDetails, key = { item -> item.uid})
                            { item ->
                                RequestMainComposable(item)
                            }
                        }
                    }

                    //  If no requests available display "no requests"
                    AnimatedVisibility(visible = listA.requestsInCount<1,
                        modifier = Modifier.weight(0.9f))
                    {
                        Column(modifier = Modifier
                            .weight(0.9f)
                            .fillMaxSize(), verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally)
                        {
                            NoEntryComposable("Requests")
                        }
                    }
                }
            }

            //  Displays banner ad
            /*Column(modifier = Modifier.fillMaxWidth()
                .weight(0.1f),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom
            )
            {
                AndroidView(factory = {
                        context -> AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(AdRequest.Builder().build())
                }
                }
                )
            }*/

            //  Navigation bar
            Row(modifier = Modifier
                .height((0.07f * screenheight).dp)
                .fillMaxWidth())
            {
                navigationComposable()
            }
        }

        //  pop up option menus
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End)
        {
            //  space between menu and top bar
            Spacer(modifier = Modifier
                .height((0.07f * screenheight).dp)
                .fillMaxWidth())

            //  option menu
            Column(modifier = Modifier.fillMaxWidth(0.4f),
                horizontalAlignment = Alignment.CenterHorizontally
                )
            {
                AnimatedVisibility(visible = showMainMenu.showMainMenu)
                {
                    MenuComposable()
                }
            }
        }
    }
}

//  Composable object for singular request
//  Displays, profile picture, and information of requester
//  Along with action buttons to accept or decline request
@Composable
fun RequestMainComposable(item: RequesterClass)
{
    //  Enables the objects to animate in and out
    val rowValue = remember{ androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(Unit)
    {
        rowValue.animateTo(1f, animationSpec = tween(1000, easing = FastOutLinearInEasing))
    }

    Card(modifier = Modifier
        .requiredHeight(150.dp)
        .padding(top = 8.dp)
        .alpha(rowValue.value),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    )
    {
        Column(modifier = Modifier, verticalArrangement = Arrangement.Center)
        {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .weight(0.6f),
                verticalAlignment = Alignment.CenterVertically)
            {
                val requesterUID = item.uid
                //  Displays profile picture
                Column(modifier = Modifier
                    .weight(0.3f)
                    .fillMaxWidth()
                    .padding(top = 0.dp))
                {
                    RequesterPicComposable(requesterUID)
                }

                //  Displays information like name, age, school, gender
                Column(modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth()
                    .padding(top = 0.dp))
                {
                    RequesterDetailsComposable(item)
                }
            }

            //  Displays action buttons accept or decline
            Row(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(0.dp)
                .weight(0.4f),
                verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.End)
            {
                RespondComposable(item)
            }
        }
    }
}

//  Displays profile picture
@Composable
fun RequesterPicComposable(requesterUID: String)
{
    val color = MaterialTheme.colorScheme.secondary
    Column(modifier = Modifier
        .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally)
    {
        Card(modifier = Modifier
            .padding(8.dp)
            .aspectRatio(1f)
            .border(1.dp, color = color, shape = RoundedCornerShape(50)),
            shape = RoundedCornerShape(50))
        {
            appViewModel.requestImagesMap[requesterUID]?.let {
                Image(
                    it,
                    contentDescription = "user profile picture", contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize())
            }
        }
    }
}

//  Displayed if no requests available
@Composable
fun NoRequestsComposable()
{
    Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center)
    {
        Text(text = "No Requests", fontSize = 24.sp, modifier = Modifier.alpha(0.5f),
            fontFamily = bison, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
    }
}

//  Displays user information
@Composable
fun RequesterDetailsComposable(requesterMap: RequesterClass)
{
    Column(modifier = Modifier.padding(top = 0.dp, bottom = 0.dp))
    {
        //  username, age, gender
        Surface(modifier= Modifier
            .weight(0.05f)
            .fillMaxSize()
            .padding(top = 0.dp, start = 0.dp, end = 0.dp, bottom = 0.dp),
            color = Color.Transparent, shape = RoundedCornerShape(0.dp)
        )
        {
            Row(verticalAlignment = Alignment.CenterVertically, modifier= Modifier
                .weight(0.07f)
                .fillMaxSize())
            {
                Row(horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f))
                {
                    Text(text = requesterMap.username, modifier = Modifier.padding(start = 0.dp),
                        fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                    Text(text = requesterMap.age, modifier = Modifier
                        .alpha(0.6f)
                        .padding(start = 12.dp),
                        fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                }

                Row(horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.1f))
                {
                    Log.d("gender", requesterMap.gender)
                    Icon(painter = painterResource(id =
                    if(requesterMap.gender=="male"){
                        R.drawable.male_svgrepo_com__1_
                    }else{
                        R.drawable.female_svgrepo_com
                    }
                    ),
                        contentDescription = "gender icon",
                        modifier= Modifier
                            .scale(0.5f)
                            .padding(end = 8.dp))
                }
            }
        }

        Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(end = 12.dp))

        //  major
        Surface(modifier= Modifier
            .weight(0.05f)
            .padding(top = 0.dp, start = 0.dp, end = 0.dp, bottom = 0.dp),
            color = Color.Transparent, shape = RoundedCornerShape(0.dp)
        )
        {
            Row(modifier= Modifier
                .weight(0.07f)
                .fillMaxWidth()
                .padding(start = 0.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
                Row(modifier= Modifier
                    .weight(0.7f)
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(text = requesterMap.major, modifier = Modifier.padding(start = 0.dp),
                        fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, letterSpacing = 1.sp)
                    Text(text = "|", modifier = Modifier.padding(start = 0.dp),
                        fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, letterSpacing = 1.sp)
                    Text(text = "${requesterMap.year}'Year", modifier = Modifier.padding(start = 0.dp),
                        fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, letterSpacing = 1.sp)
                    Text(text = "|", modifier = Modifier.padding(start = 0.dp),
                        fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, letterSpacing = 1.sp)
                    Text(text = requesterMap.school, modifier = Modifier.padding(end = 12.dp),
                        fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, letterSpacing = 1.sp)
                }
            }
        }
    }
}

//  Displays action buttons accept and decline
@Composable
fun RespondComposable(requesterMap: RequesterClass)
{
    Surface(modifier= Modifier
        .padding(top = 0.dp, start = 0.dp, end = 12.dp, bottom = 0.dp)
        .requiredHeight(40.dp)
        .requiredWidth(200.dp),
        color = Color.Transparent, shape = RoundedCornerShape(0.dp)
    )
    {
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(0.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically)
        {
            //  Decline button
            ElevatedButton(onClick = { appViewModel.declineRequest(requesterMap.uid) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.8f)
                    .weight(0.5f)
                    .padding(end = 2.dp),
            )
            {
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically)
                {
                    Icon(painter = painterResource(id = R.drawable.decline_cross),
                        contentDescription = "decline icon",
                        modifier= Modifier)
                }
            }

            //  Accept button
            ElevatedButton(onClick = { appViewModel.acceptRequest(requesterMap.uid, requesterMap.key) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.8f)
                    .weight(0.5f)
                    .padding(start = 2.dp),
            )
            {
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically)
                {
                    Icon(painter = painterResource(id = R.drawable.accept_tick),
                        contentDescription = "accept icon",
                        modifier= Modifier,
                    )
                }
            }
        }
    }
}