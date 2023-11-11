package com.fishinspace.projectcosmichamster.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import java.util.Comparator

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MessagesScreen()
{
    val friendsList by appViewModel.friendsDetailList.collectAsState()
    Log.d("Friends_Count", friendsList.friendsCount.toString())
    var deleteMode by remember { mutableStateOf("none") }
    val screenheight = LocalConfiguration.current.screenHeightDp
    val showMainMenu by appViewModel.uiState.collectAsState()

    Box()
    {
        Column(modifier = Modifier.fillMaxSize())
        {
            val messagesAvailable = friendsList.friendsCount>0

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
                        Text(text = "MESSAGES", fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = 1.sp)
                    }
                }
            }

            Column(modifier = Modifier
                .weight(0.9f)
                .fillMaxWidth(), verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                AnimatedVisibility(visible = messagesAvailable)
                {
                    LazyVerticalGrid(columns = GridCells.Fixed(1))
                    {
                        /*item {
                            MockComposable(onDrag = {Log.d("drag position", it.toString());
                            deleteMode = if(it<0){true}else{false}})
                        }*/
                        //var mockUser = UserClass("uhisdn", "sdasd", "James", "24", "hidas", "hk", "jdas", "js", "jd")
                        items(items = friendsList.friendsDetails, key = { item -> item.uid})
                        {
                                item -> ChatsComposable(item,
                            onDrag = {delta, id -> Log.d("drag position", delta.toString());
                                deleteMode = if(deleteMode==id){"none"}else{id}},
                            deleteMode
                        )
                        }
                    }
                }

                AnimatedVisibility(visible = !messagesAvailable)
                {
                    Column(modifier = Modifier
                        .weight(0.9f)
                        .fillMaxSize(), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        NoEntryComposable("Messages")
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
                    adUnitId = "ca-app-pub-7634280460905037/5922114437"
                    loadAd(AdRequest.Builder().build())
                }
                }
                )
            }*/

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

@Composable
fun MockComposable(onDrag: (Float) -> Unit)
{
    ElevatedCard(modifier = Modifier
        .requiredHeight(40.dp)
        .fillMaxWidth()
        .draggable(state = DraggableState(onDelta = onDrag), orientation = Orientation.Horizontal))
    {
        Text(text = "sucker")
    }
}

//  Entry name could be, messages, discoveries, posts, requests
@Composable
fun NoEntryComposable(entryName: String)
{
    Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Text(text = "No $entryName", fontSize = 24.sp, modifier = Modifier.alpha(0.5f),
            fontFamily = bison, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ChatsComposable(friend: UserClass, onDrag: (Float, String) -> Unit, deleteMode: String)
{
    var highlightsCount = 0
    var lastMessage = ""
    var hasUnread = false
    var lastMessageDate = ""
    var unreadCount = 0
    //  Grab user last message or messages
    //  Try unread, if empty then grab from read
    val messages by appViewModel.messageHighlights.collectAsState()
    Log.d("Highlights_Count", messages.messageHighlightsCount.toString())

    if(messages.messageHighlightsDetails != null)
    {
        //  Check if has a highlight already setup
        hasUnread = messages.messageHighlightsDetails.containsKey(friend.uid)
        val unread by appViewModel.messagesUnreadMap.collectAsState()

        //  Check if has unread
        hasUnread = unread.messagesUnreadDetails.containsKey(friend.uid)

        //  If unread buffer exists but is empty, flag no unread messages
        if(hasUnread)
        {
            hasUnread = unread.messagesUnreadDetails[friend.uid]?.size!! > 0
        }
    }

    if(hasUnread)
    {
        Log.d("Highlights_Count", messages.messageHighlightsDetails[friend.uid]!!.message.toString())
        val unread by appViewModel.messagesUnreadMap.collectAsState()
        highlightsCount  = unread.messagesUnreadDetails[friend.uid]?.size!!.toInt()
    }

    if(highlightsCount>0)
    {
        lastMessage = messages.messageHighlightsDetails[friend.uid]!!.message.toString()
        lastMessageDate = getTime(messages.messageHighlightsDetails[friend.uid]!!.date)
    }   else{
        val messages by appViewModel.messagesMap.collectAsState()
        if(messages.messagesDetails.containsKey(friend.uid))
        {
            Log.d("Highlights_Count", messages.messagesDetails[friend.uid]!!.size.toString())
            highlightsCount  = messages.messagesDetails[friend.uid]!!.size
            lastMessage = messages.messagesDetails[friend.uid]!!.last().message
            lastMessageDate = getTime(messages.messagesDetails[friend.uid]!!.last().date)
        } else{ lastMessage = "-- Say hi --"; lastMessageDate = "--:--"}
    }

    val rowValue = remember{ androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(Unit)
    {
        rowValue.animateTo(1f, animationSpec = tween(500, easing = FastOutLinearInEasing))
    }
    var userID = friend.uid

    val pic = appViewModel.friendProfilePictures[friend.uid]
    Card(modifier = Modifier
        .requiredHeight(75.dp)
        .alpha(rowValue.value)
        .padding(start = 0.dp, end = 2.dp, bottom = 0.dp)
        .draggable(
            state = DraggableState(onDelta = { onDrag(it, userID) }),
            orientation = Orientation.Horizontal,
        ),
        shape = RoundedCornerShape(0.dp),
        colors =  CardDefaults.cardColors(
            if(hasUnread)
            {
                MaterialTheme.colorScheme.secondaryContainer
            }   else
            {
                MaterialTheme.colorScheme.surface
            }
        )
    )
    {
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.weight(0.9f))
        {
            Row(modifier = Modifier
                .weight(0.6f)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .clickable {
                    appViewModel.setActiveChat(friend.uid)
                    appViewModel.updateOpenChat(friend.uid)
                    appViewModel.clearUnread(friend.uid)
                    navController.navigate(Destination.ChatScreen.route)
                })
            {
                Column(modifier = Modifier
                    .weight(0.15f)
                    .padding(start = 8.dp), horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Card(shape = RoundedCornerShape(100), modifier = Modifier
                        .padding(4.dp)
                        .weight(0.1f)
                        .aspectRatio(1f)
                        .border(
                            1.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(50)
                        ),
                        colors =  CardDefaults.cardColors(containerColor = Color.Transparent))
                    {
                        if (pic != null) {
                            Image(pic,
                                contentDescription = "user profile picture",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .scale(1f),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Column(modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 0.dp))
                {
                    Row(modifier = Modifier
                        .weight(0.6f)
                        .padding(start = 8.dp), verticalAlignment = Alignment.Bottom)
                    {
                        Text(text = friend.username, fontSize = 20.sp, modifier = Modifier
                            .weight(0.1f)
                            .padding(top = 10.dp, start = 4.dp),
                        fontFamily = yanone)
                    }

                    Row(modifier = Modifier
                        .weight(0.6f)
                        .padding(start = 8.dp, top = 0.dp))
                    {
                        Text(text = lastMessage,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .weight(0.1f)
                                .alpha(
                                    if (hasUnread) {
                                        0.9f
                                    } else {
                                        0.6f
                                    }
                                )
                                .padding(start = 4.dp),
                            fontFamily = yanone
                        )
                    }

                }

                Column(modifier = Modifier
                    .weight(0.1f)
                    .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxSize()
                    )
                    {
                        Text(text = lastMessageDate, fontSize = 18.sp, fontFamily = bison, fontWeight = FontWeight.SemiBold)
                    }

                    AnimatedVisibility(visible = hasUnread, modifier = Modifier.weight(0.5f))
                    {
                        Surface(modifier = Modifier
                            .aspectRatio(1f)
                            .padding(8.dp),
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        {
                            Column(verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            )
                            {
                                Text(text = highlightsCount.toString(), fontSize = 14.sp, fontFamily = bison, fontWeight = FontWeight.SemiBold,)
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = deleteMode==userID, modifier = Modifier.weight(0.06f))
            {
                ElevatedCard(modifier = Modifier
                    .weight(0.06f)
                    .alpha(1f)
                    .clickable { appViewModel.unfriend(friend.uid, friend.key) },
                    shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.onError)
                )
                {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 0.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.profile_remove_svgrepo_com),
                            contentDescription = "remove icon", modifier = Modifier
                                .fillMaxSize(0.6f)
                                .clipToBounds()
                        )
                    }
                }
            }
        }

        Divider(modifier = Modifier.fillMaxWidth(1f).alpha(0.8f),
            color = MaterialTheme.colorScheme.onSurface.copy(0.3f),
            thickness = Dp.Hairline)
    }
}