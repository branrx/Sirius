package com.fishinspace.projectcosmichamster.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun RequestsScreen()
{
    val listA by appViewModel.requestsInDetailList.collectAsState()
    val requestsInCount by remember { mutableStateOf(appViewModel.requestsInCount) }
    Log.d("sampled -- Request", listA.requestsInDetails.size.toString())

    Column(modifier = Modifier.fillMaxSize())
    {
        //  top bar
        Row(modifier = Modifier
            .requiredHeight(60.dp)
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
            Surface(modifier = Modifier.fillMaxWidth(0.35f).padding(8.dp), shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.inversePrimary)))
            )
            {
                Row(modifier = Modifier.fillMaxSize().padding(0.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center)
                {
                    Text(text = "REQUESTS", fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = 1.sp)
                }
            }
        }

        Card(modifier = Modifier
            .weight(0.9f)
            .padding(8.dp)
            .shadow(0.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        )
        {
            //  user profile
            Column(modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(0.dp),
                //.shadow(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                var requestsAvailable = listA.requestsInCount>0
                AnimatedVisibility(requestsAvailable)
                {
                    LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = Modifier.fillMaxSize())
                    {
                        items(items = listA.requestsInDetails, key = { item -> item.uid})
                        { item ->
                            requestMainComposable(item)
                        }
                    }
                }
                AnimatedVisibility(!requestsAvailable)
                {
                    noRequestsComposable()
                }
            }
        }

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

        //  navigation bar
        Row(modifier = Modifier
            .requiredHeight(60.dp)
            .fillMaxWidth())
        {
            navigationComposable()
        }
    }
}

@Composable
fun requestMainComposable(item: RequesterClass)
{
    val rowValue = remember{ androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(Unit)
    {
        rowValue.animateTo(1f, animationSpec = tween(1000, easing = FastOutLinearInEasing))
    }
    Card(modifier = Modifier
        .requiredHeight(150.dp)
        .padding(top = 8.dp)
        .alpha(rowValue.value)
        //.border(Dp.Hairline, color = brown, shape = RoundedCornerShape(8.dp)),
        ,colors = CardDefaults.cardColors(containerColor = Color.Transparent)
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
                var requesterUID = item.uid
                //  profile picture
                Column(modifier = Modifier
                    .weight(0.3f)
                    .fillMaxWidth()
                    .padding(top = 0.dp))
                {
                    requesterPicComposable(requesterUID)
                }
                Column(modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth()
                    .padding(top = 0.dp))
                {
                    requesterDetailsComposable(item)
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(0.dp)
                .weight(0.4f),
                verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.End)
            {
                respondComposable(item)
            }
        }
    }
}

@Composable
fun requesterPicComposable(requesterUID: String)
{
    var color = MaterialTheme.colorScheme.inversePrimary
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



@Composable
fun noRequestsComposable()
{
    Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center)
    {
        Text(text = "No Requests", fontSize = 24.sp, modifier = Modifier.alpha(0.5f),
            fontFamily = bison, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
    }
}

@Composable
fun acceptRejectComposable()
{
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp), horizontalArrangement = Arrangement.Center)
    {
        //  reject button
        Button(onClick = { /*appViewModel.declineRequest()*/ },
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .padding(end = 2.dp),
            colors = ButtonDefaults.outlinedButtonColors(Color(0xcfff6969)),shape = RoundedCornerShape(30)
        )
        {
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically)
            {
                Text(text = "DECLINE",color = Color.White)
                //Image(painter = painterResource(id = R.drawable.thumbs_down_skin_2_svgrepo_com),
                //    contentDescription = "thumbs down")
            }
        }

        Button(onClick = { /*appViewModel.acceptRequest()*/ },
            modifier = Modifier
                .fillMaxWidth(0.415f)
                .padding(start = 2.dp),
        colors = ButtonDefaults.outlinedButtonColors(Color(0xcf98eecc)),shape = RoundedCornerShape(30)
        )
        {
            Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically)
            {
                //Image(painter = painterResource(id = R.drawable.thumbs_up_skin_2_svgrepo_com),
                //    contentDescription = "thumbs down")
                Text(text = "ACCEPT",color = Color.White)
            }
        }
    }
}

@Composable
fun requesterDetailsComposable(requesterMap: RequesterClass)
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
                //.background(Color(0XFFdfbac9))
                .weight(0.07f)
                .fillMaxWidth()
                .padding(start = 0.dp),
                verticalAlignment = Alignment.CenterVertically)
            {
                /*Row(modifier= Modifier
                    .weight(0.1f)
                    .fillMaxWidth())
                {
                    Icon(painter = painterResource(id = R.drawable.graduation_cap_solid_svgrepo_com),
                        contentDescription = "major icon",
                        modifier= Modifier.scale(0.35f))
                }*/
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

@Composable
fun respondComposable(requesterMap: RequesterClass)
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
            //  reject button
            ElevatedButton(onClick = { appViewModel.declineRequest(requesterMap.uid) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.8f)
                    .weight(0.5f)
                    .padding(end = 2.dp),
                //shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp, bottomStart = 8.dp, bottomEnd = 0.dp)
                //colors = ButtonDefaults.outlinedButtonColors(Color(0xcfff6969)),shape = RoundedCornerShape(30)
            )
            {
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically)
                {
                    Icon(painter = painterResource(id = R.drawable.decline_cross),
                        contentDescription = "decline icon",
                        modifier= Modifier)
                    //Image(painter = painterResource(id = R.drawable.thumbs_down_skin_2_svgrepo_com),
                    //    contentDescription = "thumbs down")
                }
            }

            ElevatedButton(onClick = { appViewModel.acceptRequest(requesterMap.uid, requesterMap.key) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.8f)
                    .weight(0.5f)
                    .padding(start = 2.dp),
                //shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp, bottomStart = 0.dp, bottomEnd = 8.dp)
                //colors = ButtonDefaults.outlinedButtonColors(Color(0xaf98eecc)),shape = RoundedCornerShape(30)
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