package com.fishinspace.projectcosmichamster.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController
import com.fishinspace.projectcosmichamster.ui.theme.ProjectCosmicHamsterTheme
import java.util.Collections

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatScreen()
{
    var msg by remember { mutableStateOf("")}

    //  controls the count of messages
    val openChatInfo by appViewModel.openChat.collectAsState()
    val openChatMessages by appViewModel.messagesMap.collectAsState()

    Log.d("messages count", openChatInfo.messageCount.toString())

    Column(modifier = Modifier
        .fillMaxSize()
        .imePadding())
    {
        //  top bar
        Row(modifier = Modifier
            .requiredHeight(60.dp)
            .fillMaxWidth())
        {
            chatBarComposable()
        }

        //  chats
        Column(modifier = Modifier
            .weight(0.9f)
            .imePadding())
        {
            var firstVisible = if(openChatInfo.messageCount>0){openChatInfo.messageCount}else{0}

            LazyVerticalGrid(columns = GridCells.Fixed(1), state = LazyGridState(firstVisibleItemIndex = firstVisible) ,
            modifier = Modifier
                .imePadding()
                .consumeWindowInsets(WindowInsets(bottom = 24.dp)))
            {
                //var tempList = Collections.unmodifiableList(openChatMessages.messagesDetails[openChatInfo.chatID])
                if(openChatMessages.messagesDetails.containsKey(openChatInfo.chatID))
                {
                    var tempList = Collections.unmodifiableList(openChatMessages.messagesDetails[openChatInfo.chatID])
                    Log.d("Collection size", tempList.size.toString())
                    var count = 0

                    items(items = tempList, key = { item -> item.date.toLong() })
                    {
                            item -> messageComposable(count, item)
                        count+=1
                    }
                }
            }
        }

        //  message, input field, send
        ElevatedCard(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 0.dp, top = 0.dp),
        )
        {
            messageInputComposable(msg, {msg=it}, msgReset = {msg = ""})
        }
    }
}

@Composable
fun chatBarComposable()
{
    var primaryColor = MaterialTheme.colorScheme.inversePrimary
    var userID = appViewModel.friendsDetailList.collectAsState().value.friendsDetails[appViewModel.activeChatNumber]
    var userObj = appViewModel.friendsDetailList.collectAsState().value.friendsDetails[appViewModel.activeChatNumber]

    ElevatedCard(
        modifier = Modifier
            .padding(start = 0.dp, end = 0.dp)
            .shadow(0.dp),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
    )
    {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            //  logo and name of application
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
                        contentDescription = "about icon",
                        modifier = Modifier
                            .padding(top = 18.dp, bottom = 18.dp, start = 10.dp)
                    )
                }
                Card(shape = RoundedCornerShape(50), modifier = Modifier
                    .padding(4.dp)
                    .scale(0.9f)
                    .border(1.dp, color = primaryColor, shape = RoundedCornerShape(50)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface))
                {
                    appViewModel.friendProfilePictures[userID.uid]?.let {
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
                Text(userObj.username,
                        modifier = Modifier.padding(start = 4.dp, bottom = 2.dp), fontFamily = bison, letterSpacing = 1.sp,
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
                        modifier = Modifier.padding(18.dp)
                    )
                }
            }
        }
    }
}

//  Card containing the post user is replying to
@Composable
fun postReplyComposable(heading: String, note: String)
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

@Composable
fun messageComposable(index: Int, msgObj: MessageClass)
{
    var sender = msgObj.sender

    Row(horizontalArrangement = if(sender=="to"){ Arrangement.End}else{Arrangement.Start},
        modifier = Modifier.fillMaxWidth()
    )
    {
        Column(modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(0.8f),
            horizontalAlignment = if(sender=="to"){ Alignment.End}else{Alignment.Start}
        ){
            ElevatedCard()
            {
                Column()
                {
                    if(msgObj.post != "none")
                    {
                        Text(
                            text = "REPLYING TO:", modifier = Modifier
                                .padding(start = 16.dp, top = 4.dp)
                                .alpha(0.6f), fontSize = 14.sp, fontFamily = yanone,
                            letterSpacing = 1.sp, fontWeight = FontWeight.Thin
                        )

                        Column()
                        {
                            var post = msgObj.post.split("*~*")
                            postReplyComposable(post[0], post[1])
                        }
                    }

                    Row(modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 0.dp),
                        horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically)
                    {
                        Text(
                            text = msgObj.message, modifier = Modifier
                                .padding(8.dp), fontFamily = yanone, fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun messageInputComposable(msg: String, onChange: (String)->Unit, msgReset: () -> Unit)
{
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly)
    {
        ElevatedCard(modifier = Modifier
            .padding(start = 0.dp, top = 4.dp, bottom = 4.dp, end = 4.dp)
            .fillMaxWidth(0.8f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        )
        {
            Column(modifier = Modifier
                .padding(start = 0.dp)
                .fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
            {
                BasicTextField(value = msg, onValueChange = onChange, modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(8.dp),
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
            ElevatedButton(onClick = { appViewModel.sendMessage(msg); msgReset() },modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
                .padding(top = 4.dp, bottom = 4.dp),
                shape = RoundedCornerShape(topStartPercent = 16, topEndPercent = 50, bottomStartPercent = 16, bottomEndPercent = 50),
                colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.onPrimary)
            )
            {
                Icon(painter = painterResource(id = R.drawable.send_arrow),
                    contentDescription = "send icon", modifier = Modifier
                        .clipToBounds()
                        .requiredSize(20.dp)
                        .clickable { appViewModel.sendMessage(msg); msgReset() },
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
