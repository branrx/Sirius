package com.fishinspace.projectcosmichamster.ui

import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Collections

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun ChatScreen()
{
    //
    var checkIme by remember{ mutableStateOf(false) }

    //  Holds text message input
    var messageVar by remember { mutableStateOf("")}

    //  Used to partition the screen
    val screenheight = LocalConfiguration.current.screenHeightDp

    //  Grab messages received from server
    val openChatInfo by appViewModel.openChat.collectAsState()
    val openChatMessages by appViewModel.messagesMap.collectAsState()

    //  Set state for grid
    var gridState by remember { mutableStateOf(
    if(!openChatMessages.messagesDetails.containsKey(openChatInfo.chatID))
    {   LazyGridState() }
    else{ LazyGridState(firstVisibleItemIndex = openChatMessages.messagesDetails[openChatInfo.chatID]!!.size)}) }

    //  Checks if keyboard is open, then scrolls message pool
    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current
    val viewTreeObserver = view.viewTreeObserver
    DisposableEffect(viewTreeObserver) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            Log.d("Ime", "toggled")
            if(!openChatMessages.messagesDetails[openChatInfo.chatID].isNullOrEmpty())
            {
                coroutineScope.launch { gridState.scrollToItem(openChatMessages.messagesDetails[openChatInfo.chatID]!!.size); Log.d("tree obs", "scrolled") }
            }

        }
        viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    Log.d("messages count", openChatInfo.messageCount.toString())

    Column(modifier = Modifier
        .fillMaxSize())
    {
        //  Top Bar
        Row(modifier = Modifier
            .height((0.07f * screenheight).dp)
            .fillMaxWidth())
        {
            ChatBarComposable()
        }

        //  Chats
        Column(modifier = Modifier
            .weight(0.9f))
        {
            AnimatedVisibility(visible = openChatInfo.messageCount>0)
            {
                LazyVerticalGrid(columns = GridCells.Fixed(1), state = gridState ,
                    modifier = Modifier.focusTarget()
                )
                {
                    if(openChatMessages.messagesDetails.containsKey(openChatInfo.chatID))
                    {
                        //  Store messages in unmodifiable collection, because grids trip
                        //  If values changes while composing
                        val tempList = if(openChatMessages.messagesDetails.containsKey(openChatInfo.chatID))
                        {Collections.unmodifiableList(openChatMessages.messagesDetails[openChatInfo.chatID])}
                        else{   listOf() }

                        var count = 0

                        if(tempList.isNotEmpty())
                        {
                            items(items = tempList, key = { item -> item.date.toLong() })
                            {
                                    item -> MessageComposable(count, item, tempList.size)
                                count+=1
                            }
                        }
                    }
                }
            }
        }

        //  Message input field, Send button
        Surface(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 0.dp, top = 0.dp),
        )
        {
            MessageInputComposable(messageVar, {messageVar=it}, msgReset = {messageVar = ""}
            )
        }
    }
}


//  Chat Menu Bar
//  Top Bar, holds back button, profile picture and info button
@Composable
fun ChatBarComposable()
{
    //  Grab open chat (friend/user) object
    val friendID = appViewModel.friendsDetailList.collectAsState().value.friendsDetails[appViewModel.activeChatNumber]

    Surface(modifier = Modifier
            .padding(start = 0.dp, end = 0.dp)
            .shadow(0.dp),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
    )
    {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxSize()
                    .padding(start = 2.dp)
            )
            {
                Surface(modifier = Modifier
                    .clickable { navController.navigate(Destination.MessagesScreen.route); })
                {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left_svgrepo_com),
                        contentDescription = "back icon",
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp, start = 10.dp)
                    )
                }
                Card(shape = RoundedCornerShape(50), modifier = Modifier
                    .padding(4.dp)
                    .scale(0.9f)
                    .border(1.dp, color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(50)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface))
                {
                    appViewModel.friendProfilePictures[friendID.uid]?.let {
                        Image(
                            it,
                            contentDescription = "user profile picture",
                            modifier = Modifier
                                .scale(1f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Text(friendID.username,
                        modifier = Modifier
                            .padding(start = 4.dp, bottom = 2.dp), fontFamily = bison, letterSpacing = 1.sp,
                    fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
            }

            //  about page icon
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxSize()
            )
            {
                Surface(modifier = Modifier
                    .clip(shape = RoundedCornerShape(50))
                    .aspectRatio(1f)
                    .clickable { navController.navigate(Destination.AboutScreen.route) },
                )
                {
                    Icon(
                        painter = painterResource(id = R.drawable.danger_circle_svgrepo_com),
                        contentDescription = "about icon",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

}

//  Card for post replies
//  Holds the post to which the user is replying
@Composable
fun PostReplyComposable(heading: String, note: String)
{
    Card(modifier = Modifier.padding(start = 14.dp, end = 8.dp, top = 4.dp),
        shape = RoundedCornerShape(8.dp)
    )
    {
        Column()
        {
            Text(heading, modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp),
                fontFamily = yanone, fontWeight = FontWeight.Bold)
            Text(note,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 4.dp),
                fontFamily = yanone)
        }
    }
}

//  Displays singular chat bubbles per message sent or received
@Composable
fun MessageComposable(index: Int, messageObj: MessageClass, lastIndex: Int)
{
    Row(horizontalArrangement = if(messageObj.sender=="to"){ Arrangement.End}else{Arrangement.Start},
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        Column(modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(0.8f),
            horizontalAlignment = if(messageObj.sender=="to"){ Alignment.End}else{Alignment.Start}
        ){
            ElevatedCard()
            {
                Column()
                {
                    if(messageObj.post != "none")
                    {
                        Text(
                            text = "REPLYING TO:", modifier = Modifier
                                .padding(start = 16.dp, top = 4.dp)
                                .alpha(0.6f), fontSize = 14.sp, fontFamily = yanone,
                            letterSpacing = 1.sp, fontWeight = FontWeight.Thin
                        )

                        Column()
                        {
                            val post = messageObj.post.split("*~*")
                            PostReplyComposable(post[0], post[1])
                        }
                    }

                    Row(modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 0.dp),
                        horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically)
                    {
                        Text(
                            text = messageObj.message, modifier = Modifier
                                .padding(8.dp), fontFamily = yanone, fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

//  Message input field and send button
@Composable
fun MessageInputComposable(msg: String, onChange: (String)->Unit, msgReset: () -> Unit)
{
    val context = LocalContext.current

    Row(modifier = Modifier
        .fillMaxWidth()
        .imePadding(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly)
    {
        ElevatedCard(modifier = Modifier
            .padding(start = 12.dp, top = 4.dp, bottom = 4.dp, end = 4.dp)
            .fillMaxWidth(0.8f)
            .imePadding(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        )
        {
            Column(modifier = Modifier
                .padding(start = 0.dp)
                .fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
            {
                BasicTextField(value = msg, onValueChange = onChange, modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(8.dp)
                    .imePadding(),
                    maxLines = 4,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, baselineShift = BaselineShift(0f),
                        fontSize = 16.sp),
                    cursorBrush = Brush.sweepGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary))
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.Center)
        {
            Surface(modifier = Modifier
                .clickable { appViewModel.sendMessage(msg); msgReset() }
                .fillMaxWidth()
                .clipToBounds()
                .clipToBounds()
                .padding(12.dp)
                .aspectRatio(1f),
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.secondary,
                border = BorderStroke(2.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondary)))
            )
            {
                Icon(painter = painterResource(id = R.drawable.send_arrow),
                    contentDescription = "send icon", modifier = Modifier
                        .clipToBounds()
                        .padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 8.dp)
                        .clickable {
                            if (msg.isNotEmpty()) {
                                appViewModel.sendMessage(msg); msgReset();
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "Cannot send blank message.",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                )
            }
        }
    }
}
