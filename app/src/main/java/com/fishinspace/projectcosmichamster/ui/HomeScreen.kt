package com.fishinspace.projectcosmichamster.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController
import com.fishinspace.projectcosmichamster.ui.theme.ProjectCosmicHamsterTheme
import java.util.concurrent.Executors

var yellow = Color(0XFFffe3c0)
var pink = Color(0XFFdfbac9)
var green = Color(0Xff96ead7)
var darkerGreen = Color(0Xff68c4af)
var brown = Color(0Xffb2b2b2)
var peach = Color(0Xffffa291)

val filterOptions = mapOf(0 to "mine", 1 to "academic", 2 to "accommodation")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen()
{
    var filterBy by remember { mutableStateOf("none") }
    var currentView by remember { mutableStateOf("discover")}
    val listA by appViewModel.discoveredDetailList.collectAsState()
    val postsList by appViewModel.postsList.collectAsState()
    val postsCount = postsList.postsCount
    val usersCount = listA.discoveredCount
    Log.d("discovered count -- Request", listA.discoveredCount.toString())
    Log.d("posts count", postsList.postsCount.toString())

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally)
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
            Surface(modifier = Modifier
                .clip(RoundedCornerShape(50))
                .fillMaxWidth(0.35f)
                .clickable {
                    currentView = if (currentView == "discover") {
                        "board"
                    } else {
                        "discover"
                    }
                }
                .padding(8.dp), shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.inversePrimary))),
                color = MaterialTheme.colorScheme.background
            )
            {
                Row(modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center)
                {
                    Text(text = currentView, fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = 1.sp)
                }
            }
        }

        //  Filter composable
        AnimatedVisibility(visible = currentView == "board",
            modifier = Modifier
                .weight(0.15f))
        {
            Surface(modifier = Modifier
                .padding(start = 4.dp, end = 4.dp),
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                shape = RoundedCornerShape(16.dp)
            )
            {
                Column(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                )
                {
                    Row(modifier = Modifier
                        .weight(0.5f)
                        .padding(4.dp)
                        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start)
                    {
                        Icon(painter = painterResource(id = R.drawable.filter_svgrepo_com), contentDescription = "filter posts icon",
                            modifier = Modifier
                                .clipToBounds()
                                .fillMaxHeight()
                                .padding(start = 10.dp, top = 3.5.dp, bottom = 3.5.dp)
                                .alpha(0.8f))
                        Text("filter", modifier = Modifier
                            .padding(start = 8.dp, end = 4.dp)
                            .alpha(0.8f),
                            fontFamily = bison,
                            letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .padding(0.dp)
                            .alpha(0.0f)
                    )

                    LazyHorizontalGrid(rows = GridCells.Fixed(1),
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(0.6f)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.Start)
                    {
                        items(filterOptions.size)
                        {
                                item ->  FilterComposable(item, onClick = { if(filterBy==it){filterBy="none"}else{filterBy=it} }, filterBy)
                        }
                    }
                }
            }
        }

        //  Holds the zone information composable
        Row(modifier = Modifier
            .weight(0.2f)
            .fillMaxWidth()
            .padding(top = 4.dp)
            //.background(Color.Gray)
            .clip(RoundedCornerShape(16.dp)),
            horizontalArrangement = Arrangement.SpaceAround)
        {
            //  Major zone
            Column(modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                ZoneInfoComposable(type = "major", postsCount, usersCount)
            }
        }

        var discoveredAvailable = listA.discoveredCount>0
        //  shows discovered users if available
        //  main screen
        Surface(modifier = Modifier
            .weight(0.9f)
            .padding(8.dp)
            .shadow(0.dp),
            shape = RoundedCornerShape(12.dp),
        )
        {
            //  user profile
            Column(modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                AnimatedVisibility(visible = /*discoveredAvailable*/true)
                {
                    LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = Modifier.fillMaxSize())
                    {
                        if(currentView == "discover")
                        {
                            //  Shows the people in the zone
                            items(items = listA.discoveredDetails, key = { item -> item.uid})
                            { item ->
                                discoverMainComposable(item)
                            }
                        }

                        if(currentView == "board")
                        {
                            var posts: List<PostClass> = listOf()

                            if(filterBy!="none")
                            {
                                posts = FilterBy(posts = postsList.postsList, filterBy = filterBy)
                            }   else
                            {
                                posts = postsList.postsList
                            }

                            items(items = posts, key = { item -> item.userID})
                            {
                                    item ->
                                Column(/*modifier = Modifier.animateItemPlacement(spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow))*/)
                                {
                                    boardMessageComposable(item)
                                }
                            }
                        }
                    }
                }

                //  no messages composable
                AnimatedVisibility(visible = !discoveredAvailable)
                {
                    noDiscoveredComposable()
                }
            }
        }

        /*Column(modifier = Modifier
            .fillMaxWidth()
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

        Column(modifier = Modifier
            .requiredHeight(60.dp)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            ElevatedButton(onClick = { navController.navigate(Destination.PostScreen.route) }, modifier = Modifier.fillMaxWidth(0.5f))
            {
                Text(text = "post", fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, fontSize = 20.sp, letterSpacing = 1.sp)
            }
        }

        //  navigation bar
        Column(modifier = Modifier
            .requiredHeight(60.dp)
            .fillMaxWidth())
        {
            navigationComposable()
        }
    }
}

@Composable
fun FilterComposable(index: Int, onClick: (String)->Unit, filterBy: String)
{
    var options = listOf<String>()
    var filter = filterOptions[index].toString()

    Surface(modifier = Modifier
        .padding(start = 8.dp)
        .clip(RoundedCornerShape(30))
        .clickable { onClick(filter) },
        shape = RoundedCornerShape(100),
        border = BorderStroke(1.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.inversePrimary, MaterialTheme.colorScheme.inversePrimary))),
        color = if(filterBy==filter){MaterialTheme.colorScheme.inversePrimary}else{MaterialTheme.colorScheme.surface}
    )
    {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 12.dp, end = 12.dp))
        {
            Text(text = filter, modifier = Modifier.padding(),
                fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = 1.sp)
        }
    }
}

@Composable
fun boardMessageComposable(post: PostClass)
{
    var isReplying by remember { mutableStateOf(false)}
    var onDeletePost by remember { mutableStateOf(false) }
    val rowValue = remember{ androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(Unit)
    {
        rowValue.animateTo(1f, animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
    }

    var postCat = ""
    var icon = 0
    when(post.cat)
    {
        "accommodation" -> { icon = R.drawable.home_svgrepo_com__1_; postCat = "accommodation" }
        "academic" -> { icon = R.drawable.square_academic_cap_svgrepo_com; postCat = "academic"}
        "miscellaneous" -> { icon = R.drawable.emoji_funny_square_svgrepo_com; postCat = "miscellaneous" }
        "buy/sell" -> { icon = R.drawable.cart_large_2_svgrepo_com; postCat = "buy/sell"}
    }

    ElevatedCard(modifier = Modifier
        .padding(top = 8.dp)
        .scale(1f)//rowValue.value
        .clip(
            RoundedCornerShape(
                bottomStart = 8.dp,
                topEnd = 8.dp,
                bottomEnd = 8.dp,
                topStart = 4.dp
            )
        )
        .clickable {
            if (!isReplying) {
                isReplying = true
            }
        },
        shape = RoundedCornerShape(bottomStart = 8.dp, topEnd = 8.dp, bottomEnd = 8.dp, topStart = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    )
    {
        //boardPicComposable("requesterUID")
        Column(modifier = Modifier.animateContentSize(), verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally)
        {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .requiredHeight(32.dp)
                ,verticalAlignment = Alignment.Top)
            {
                Surface(modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 0.dp)
                    .padding(bottom = 0.dp),
                    shape = RoundedCornerShape(topEndPercent = 0),
                    color = Color.Transparent
                )
                {
                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 12.dp, end = 0.dp),
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Text(text = post.username, fontFamily = yanone,
                            fontWeight = FontWeight.SemiBold, fontSize = 18.sp, letterSpacing = 1.sp)
                    }
                }

                Spacer(modifier = Modifier.weight(0.5f))

                //  timestamp, or time till expiry
                Surface(modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight()
                    .padding(end = 0.dp)
                    .padding(bottom = 0.dp),
                    shape = RoundedCornerShape(topEndPercent = 25, bottomStartPercent = 0),
                    color = Color.Transparent
                )
                {
                    Column(modifier = Modifier
                        .padding(end = 10.dp), horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Text(text = "5HRS", modifier = Modifier.padding(2.dp),
                            fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    }

                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .requiredHeight(32.dp)
                ,verticalAlignment = Alignment.CenterVertically)
            {
                Column()
                {
                    Icon(painter = painterResource(id = icon), contentDescription = "home icon",
                    modifier = Modifier.padding(6.dp))
                }

                //  category name
                Surface(modifier = Modifier.padding(start = 8.dp),
                    shape = RoundedCornerShape(25),
                    border = BorderStroke(1.dp, brush = Brush.linearGradient(listOf(peach, MaterialTheme.colorScheme.inversePrimary))),
                )
                {
                    Column()
                    {
                        Text(text = postCat, modifier = Modifier.padding(4.dp),
                            fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }
                }

                //  category: specifier
                Column(modifier = Modifier.padding(start = 8.dp))
                {
                    Text(text = "is looking for ${post.specifier}", modifier = Modifier.padding(2.dp),
                        fontFamily = yanone, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                }
            }

            Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 0.dp)
                    .alpha(
                        if (isReplying) {
                            0.3f
                        } else {
                            0f
                        }
                    ))

            //  show post description if post is clicked
            when(isReplying)
            {
                true -> { postDescription(post.note) }
                else -> { /*startReplyComposable(onReplyTap = {isReplying = true})*/ }
            }


            Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(top = 0.dp)
                    .alpha(0f)
            )

            //  show post reply if post is clicked
            when(isReplying)
            {
                true -> {
                    if (appViewModel.currentUserUid == post.userID) {
                        AnimatedVisibility(visible = onDeletePost)
                        {
                            ConfirmDeletePost(onDeny = { onDeletePost = false }, proceedDelete = {
                                appViewModel.deletePost();
                                onDeletePost = false; isReplying = false
                            })
                        }

                        AnimatedVisibility(visible = !onDeletePost)
                        {
                            EditBoardPost(category = post.cat,
                                specifier = post.specifier,
                                note = post.note,
                                onDelete = { onDeletePost = true })
                        }
                    }
                    replyInputComposable(onCancelReply = {
                        isReplying = false; onDeletePost = false
                    },
                        post,
                        onSend = { isReplying = false },
                        onDeletePost = onDeletePost
                    )
                }
                else -> { startReplyComposable(onReplyTap = {isReplying = true}) }
            }
        }
    }
}

fun FilterBy(posts: List<PostClass>, filterBy: String): List<PostClass>
{
    var tempPosts: List<PostClass> = listOf()
    tempPosts = posts.filter {
        when(filterBy)
        {
            "mine" -> { it.userID == appViewModel.currentUserUid }
            "academic" -> { it.cat == "academic" }
            "accommodation" -> { it.cat == "accommodation" }
            else -> { it.userID == appViewModel.currentUserUid }
        }
    }

    return tempPosts
}

//  Current user edit their post on the post
@Composable
fun EditBoardPost(category: String, specifier: String, note: String, onDelete: ()->Unit)
{
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(0.dp)
        .requiredHeight(32.dp)
        , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center)
    {
        //  Delete post
        Surface(modifier = Modifier
            .weight(0.5f)
            .clickable(onClick = onDelete))
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            )
            {
                Text("delete", fontFamily = bison, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        //  Edit post
        Surface(modifier = Modifier
            .weight(0.5f)
            .clickable {
                appViewModel.preparePostEdit(category, specifier, note);
                navController.navigate(Destination.EditPostScreen.route)
            },
        )
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            )
            {
                Text("edit", fontFamily = bison, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

//  Confirm delete post
@Composable
fun ConfirmDeletePost(onDeny: ()->Unit, proceedDelete: ()->Unit)
{
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.requiredHeight(64.dp)
    )
    {
        Surface(modifier = Modifier.requiredHeight(32.dp)
        )
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            )
            {
                Text("Are you sure?", fontFamily = bison, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .requiredHeight(32.dp)
            , verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center)
        {
            //  Delete post
            Surface(modifier = Modifier
                .weight(0.5f)
                .clickable(onClick = onDeny))
            {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                )
                {
                    Text("No", fontFamily = bison, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            //  Delete post
            Surface(modifier = Modifier
                .weight(0.5f)
                .clickable(onClick = proceedDelete),
                color = MaterialTheme.colorScheme.errorContainer
            )
            {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                )
                {
                    Text("Yes", fontFamily = bison, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun postDescription(note: String)
{
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
        ,verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Center)
    {
        Column(modifier = Modifier
            .weight(0.1f),
            verticalArrangement = Arrangement.Top
        )
        {
            Icon(painter = painterResource(id = R.drawable.notes_svgrepo_com), contentDescription = "home icon",
                modifier =  Modifier.scale(0.6f))
            Spacer(modifier = Modifier.weight(0.5f))
        }
        Column(modifier = Modifier
            .weight(0.9f)
            .padding(start = 8.dp, end = 8.dp))
        {
            Text(text = note, fontFamily = yanone, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
        }

    }
}

@Composable
fun startReplyComposable(onReplyTap: () -> Unit)
{
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(0.dp)
        .requiredHeight(2.dp)
        ,verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
    {
        Surface(modifier = Modifier
            .padding(0.dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(0))
            .clickable(onClick = onReplyTap),
            color = MaterialTheme.colorScheme.inversePrimary,
            shape = RoundedCornerShape(bottomEnd = 12.dp, topEnd = 0.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                modifier = Modifier)
            {
                //Text(text = "tap to reply.", fontFamily = bison, fontWeight = FontWeight.Normal, fontSize = 12.sp,
                //letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
fun replyInputComposable(onCancelReply: () -> Unit, post: PostClass, onSend: () -> Unit,
    onDeletePost: Boolean
)
{
    var reply by remember { mutableStateOf("")}

    if(appViewModel.currentUserUid != post.userID)
    {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .requiredHeight(40.dp)
            ,verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
        {
            boardReplyComposable(reply, onChange = {reply = it}, post, onSend)
        }
    }

    AnimatedVisibility(visible = !onDeletePost)
    {
        Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .alpha(0f)
                .padding(top = 0.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .requiredHeight(24.dp)
            ,verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally)
        {
            Surface(modifier = Modifier
                .padding(0.dp)
                .fillMaxSize()
                .clip(RoundedCornerShape(0))
                .clickable(onClick = onCancelReply),
                color = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                    modifier = Modifier)
                {
                    Text(text = "cancel", modifier = Modifier.padding(2.dp),
                        fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun boardReplyComposable(value: String, onChange: (String)->Unit, post: PostClass, onSend: ()->Unit)
{
    var onSurface = MaterialTheme.colorScheme.onSurface
    Row(modifier = Modifier
        .fillMaxWidth(1f), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center)
    {
        Row(modifier = Modifier)
        {
            Column(modifier = Modifier
                .weight(0.8f)
                .padding(bottom = 8.dp, start = 8.dp), verticalArrangement = Arrangement.Center)
            {
                Spacer(modifier = Modifier.weight(0.2f))
                Surface(color = Color.Transparent)
                {
                    BasicTextField(value = value, onValueChange = onChange, modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(bottom = 4.dp),
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Start),
                        cursorBrush = Brush.sweepGradient(listOf(onSurface, onSurface)),
                        singleLine = true
                    )
                }
                Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .alpha(0.3f)
                        .padding(top = 0.dp))
            }

            Row(modifier = Modifier
                .weight(0.15f),
                verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                //  send reply
                Surface(modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .scale(0.6f)
                    .clip(RoundedCornerShape(50))
                    .aspectRatio(1f)
                    .clickable {
                        appViewModel.replyToPost(
                            heading = post.specifier, note = post.note, message = value,
                            userID = post.userID
                        ); onSend()
                    },
                    color = Color.Transparent
                )
                {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                    modifier = Modifier)
                    {
                        Icon(painter = painterResource(id = R.drawable.send_arrow), contentDescription = "send reply icon",
                            modifier = Modifier
                                .clipToBounds()
                                .scale(1f)
                                .padding(8.dp))
                    }
                }

            }
        }
    }
}

//  Displays current zone information
@Composable
fun ZoneInfoComposable(type: String, postsCount: Int, usersCount: Int)
{
    var zoneType = if(type == "major"){Color.Red}else{Color.Blue}
    Surface(color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
    shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    )
    {
        Column(//modifier = Modifier.background(zoneType),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(4.dp))
        {
            Row(horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top,
                modifier = Modifier.weight(0.7f))
            {
                //  Displays the name
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(0.25f))
                {
                    Row(horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(0.7f)
                            .fillMaxWidth(0.3f))
                    {
                        Icon(painter = painterResource(id = R.drawable.notes_svgrepo_com), contentDescription = "user count icon",
                            modifier = Modifier
                                .clipToBounds()
                                .padding(2.dp))
                        Text(postsCount.toString(), fontSize = 24.sp, fontFamily = bison, fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp)
                    }

                    Row(horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.weight(0.3f))
                    {
                        Text("posts", fontSize = 12.sp, fontFamily = bison, fontWeight = FontWeight.Thin,
                            letterSpacing = 1.sp)
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(0.25f))
                {
                    Row(horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(0.7f)
                            .fillMaxWidth(0.3f))
                    {
                        Icon(painter = painterResource(id = R.drawable.profile_circle_svgrepo_com), contentDescription = "user count icon",
                            modifier = Modifier
                                .clipToBounds()
                                .padding(2.dp))
                        Text(usersCount.toString(), fontSize = 24.sp, fontFamily = bison, fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp)
                    }

                    Row(horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.weight(0.3f))
                    {
                        Text("users", fontSize = 12.sp, fontFamily = bison, fontWeight = FontWeight.Thin,
                            letterSpacing = 1.sp)
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(0.3f)
                    .background(Color.Transparent)
                    .fillMaxWidth())
            {
                Icon(painter = painterResource(id = R.drawable.map_point_svgrepo_com), contentDescription = "user count icon",
                    modifier = Modifier
                        .clipToBounds()
                        .padding(4.dp))
                Text("LEFKE", fontSize = 16.sp, fontFamily = bison, fontWeight = FontWeight.Normal,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(end = 12.dp))
            }

        }

    }

}

//  Filters
@Composable
fun postFilter()
{
    /**/
}

@Composable
fun discoverMainComposable(item: UserClass)
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
                    discoveredPicComposable(requesterUID)
                }
                Column(modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth()
                    .padding(top = 0.dp))
                {
                    discoveredDetailsComposable(item)
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(0.dp)
                .weight(0.4f),
                verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.End)
            {
                discoveredRespondComposable(item)
            }
        }
    }
}

@Composable
fun noDiscoveredComposable()
{
    Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Text(text = "No Discoveries", fontSize = 24.sp, modifier = Modifier.alpha(0.5f),
            fontFamily = bison, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
    }
}

@Composable
fun boardPicComposable(requesterUID: String)
{
    var color = MaterialTheme.colorScheme.inversePrimary
    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally)
    {
        Card(modifier = Modifier
            .padding(8.dp)
            .requiredSize(24.dp)
            .aspectRatio(1f)
            .border(1.dp, color = color, shape = RoundedCornerShape(50)),
            shape = RoundedCornerShape(50))
        {
            appViewModel.userProfilePicture.let {
                Image(
                    it,
                    contentDescription = "user profile picture", contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun discoveredPicComposable(requesterUID: String)
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
            appViewModel.discoveredImagesList[requesterUID]?.let {
                Image(
                    it,
                    contentDescription = "user profile picture", contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun discoveredDetailsComposable(requesterMap: UserClass)
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

        Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 12.dp))

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
fun discoveredRespondComposable(requesterMap: UserClass)
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

            ElevatedButton(onClick = { appViewModel.sendRequest(requesterMap.uid) },
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
                        modifier= Modifier
                    )
                }
            }
        }
    }
}

/*
Card(modifier = Modifier
            .weight(0.8f)
            .padding(2.dp)
            .draggable(state = DraggableState(onDelta = {
                if (it < -5) {
                    appViewModel.getNextDiscovered()
                }
            }), orientation = Orientation.Horizontal)
            .border(1.dp, color = brown, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        )
        {
            //  user profile
            Column(modifier = Modifier
                .weight(1f)
                .padding(bottom = 0.dp),
                //.shadow(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                Card(modifier = Modifier
                    .padding(start = 0.dp, end = 0.dp, top = 0.dp)
                    .border(
                        1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .weight(0.7f),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                {
                    userComposable()
                }

                bgColor?.let { CardDefaults.cardColors(containerColor = it) }?.let {
                    Card(modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp, top = 4.dp)
                        .weight(0.4f),
                        colors = it
                    )
                    {
                        userDetailsComposable()
                    }
                }

                //  filter
                Column(modifier = Modifier
                    .weight(0.1f))
                {
                    memoryComposable(value = valuePrompt, onChange = { valuePrompt = it })
                }

                Row(modifier = Modifier
                    .weight(0.1f)
                    .padding(2.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically)
                {
                    requestComposable()
                }
            }
        }
*/



@Composable
fun memoryComposable(value: String, onChange: (String) -> Unit)
{
    Surface(modifier = Modifier
        .padding(start = 4.dp, end = 4.dp), shape = RoundedCornerShape(12.dp)
    )
    {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        )
        {
            Column(modifier = Modifier
                .padding(top = 0.dp)
                .requiredWidth(300.dp)
                .requiredHeight(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center)
            {
                BasicTextField(value = value, onValueChange = onChange, modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.White, textAlign = TextAlign.Center),
                    cursorBrush = Brush.sweepGradient(listOf(Color.White, Color.White)),
                    decorationBox = {
                        innerTextField ->
                        if(value.isEmpty()){
                            Row(horizontalArrangement = Arrangement.Center)
                            {
                                Text("Type a prompt.", color = Color.DarkGray, fontFamily = bison, letterSpacing = 2.sp)
                            }
                        }
                        innerTextField.invoke()
                    }
                )
            }
            Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 16.dp, end = 16.dp))
        }
    }
}

@Composable
fun navigationComposable()
{
    //Divider(thickness = Dp.Hairline, color = Color.White, modifier = Modifier.padding(start=12.dp, end = 12.dp))
    ElevatedCard(modifier = Modifier
        .padding(4.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
        //color = MaterialTheme.colorScheme.background
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        )
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(0.5f))
            {
                Box {
                    Surface(color = if (appViewModel.activeWindow == 0) {
                        Color.Transparent
                    } else {
                        Color.Transparent
                    }, shape = RoundedCornerShape(50),
                        modifier = Modifier
                            //.fillMaxHeight(1f)
                            .align(Alignment.Center)
                            .aspectRatio(1f)
                            .clip(shape = RoundedCornerShape(50))
                            .fillMaxWidth(1f)
                            .clickable {
                                appViewModel.changeActiveWindow(0)
                                navController.navigate(Destination.MessagesScreen.route)
                            }
                    )
                    {

                    }
                    Icon(
                        painter = painterResource(id = R.drawable.messages_svgrepo_com),
                        contentDescription = "chats icon",
                        modifier = Modifier
                            .scale(0.5f)
                            .align(Alignment.Center)
                            .alpha(
                                if (appViewModel.activeWindow == 0) {
                                    1f
                                } else {
                                    0.5f
                                }
                            )
                            .padding(start = 0.dp),
                        //tint = if(appViewModel.activeWindow==0){ Color.Unspecified }else{ Color.Gray}
                    )

                    Divider(thickness = 2.dp, color = if (appViewModel.activeWindow == 0) {
                        MaterialTheme.colorScheme.inversePrimary
                    } else {
                        Color.Transparent
                    },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 4.dp)
                            .requiredWidth(30.dp))
                }

                //Text(text = "chats", modifier = Modifier.weight(0.2f))
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center, modifier = Modifier.weight(0.5f))
            {
                Box {
                    Surface(
                        color = if (appViewModel.activeWindow == 1) {
                            Color.Transparent
                        } else {
                            Color.Transparent
                        }, shape = RoundedCornerShape(50),
                        modifier = Modifier
                            //.fillMaxHeight(1f)
                            .align(Alignment.Center)
                            .aspectRatio(1f)
                            .clip(shape = RoundedCornerShape(50))
                            .fillMaxWidth(1f)
                            .clickable {
                                appViewModel.changeActiveWindow(1)
                                Executors
                                    .newSingleThreadExecutor()
                                    .execute {
                                        appViewModel.updateDiscovered()
                                    }
                                navController.navigate(Destination.ExploreScreen.route)
                            }
                    )
                    {

                    }
                    Icon(
                        painter = painterResource(id = R.drawable.compass_remake),
                        contentDescription = "explore icon",
                        modifier = Modifier
                            .alpha(
                                if (appViewModel.activeWindow == 1) {
                                    1f
                                } else {
                                    0.5f
                                }
                            )
                            .scale(0.5f)
                            .align(Alignment.Center),
                    )

                    Divider(thickness = 2.dp, color = if (appViewModel.activeWindow == 1) {
                        MaterialTheme.colorScheme.inversePrimary
                    } else {
                        Color.Transparent
                    },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 4.dp)
                            .requiredWidth(30.dp))
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center, modifier = Modifier.weight(0.5f))
            {
                Box {
                    Surface(
                        color = if (appViewModel.activeWindow == 2) {
                            Color.Transparent
                        } else {
                            Color.Transparent
                        }, shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .aspectRatio(1f)
                            .fillMaxWidth(1f)
                            .clip(shape = RoundedCornerShape(50))
                            .clipToBounds()
                            .clickable {
                                appViewModel.changeActiveWindow(2)
                                navController.navigate(Destination.ProfileScreen.route)
                            }
                    )
                    {

                    }
                    Icon(
                        painter = painterResource(id = R.drawable.profile_circle_svgrepo_com),
                        contentDescription = "profile icon",
                        modifier = Modifier
                            .alpha(
                                if (appViewModel.activeWindow == 2) {
                                    1f
                                } else {
                                    0.5f
                                }
                            )
                            .scale(0.5f)
                            .align(Alignment.Center)
                            .padding(end = 0.dp),
                    )

                    Divider(thickness = 2.dp, color = if (appViewModel.activeWindow == 2) {
                        MaterialTheme.colorScheme.inversePrimary
                    } else {
                        Color.Transparent
                    },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 4.dp)
                            .requiredWidth(30.dp))
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center, modifier = Modifier.weight(0.5f))
            {
                Box {
                    Surface(
                        color = if (appViewModel.activeWindow == 3) {
                            Color.Transparent
                        } else {
                            Color.Transparent
                        }, shape = RoundedCornerShape(50),
                        modifier = Modifier
                            //.fillMaxHeight(1f)
                            .align(Alignment.Center)
                            .aspectRatio(1f)
                            .clip(shape = RoundedCornerShape(50))
                            .fillMaxWidth(1f)
                            .clickable {
                                appViewModel.changeActiveWindow(3)
                                Executors
                                    .newSingleThreadExecutor()
                                    .execute {
                                        appViewModel.checkRequestAvailable()
                                        appViewModel.populateRequestsDetail()
                                    }
                                navController.navigate(Destination.RequestsScreen.route)
                            }
                    ) {}
                    Icon(
                        painter = painterResource(id = R.drawable.friend_svgrepo_com),
                        contentDescription = "profile icon",
                        modifier = Modifier
                            .alpha(
                                if (appViewModel.activeWindow == 3) {
                                    1f
                                } else {
                                    0.5f
                                }
                            )
                            .scale(0.5f)
                            .align(Alignment.Center)
                            .padding(end = 0.dp),
                    )

                    Divider(thickness = 2.dp, color = if (appViewModel.activeWindow == 3) {
                        MaterialTheme.colorScheme.inversePrimary
                    } else {
                        Color.Transparent
                    },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 4.dp)
                            .requiredWidth(30.dp))
                }
            }
        }
    }
}


@Composable
fun topBarComposable()
{
    Surface(modifier = Modifier
        .padding(start = 0.dp, end = 0.dp),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        color = Color.Transparent
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
            )
            {
                Icon(
                    painter = painterResource(id = R.drawable.cirqle_logo_3),
                    contentDescription = "app icon", modifier = Modifier.padding(top = 18.dp, bottom = 18.dp, start = 20.dp),
                )
                Text(stringResource(id = R.string.app_name), fontFamily = bison, fontWeight = FontWeight.Bold, letterSpacing = 2.sp,
                fontSize = 28.sp, modifier = Modifier.padding(start=10.dp))
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
                        contentDescription = "about icon", modifier = Modifier
                            .padding(18.dp)
                            .clip(shape = RoundedCornerShape(50))
                    )
                }

                Surface(modifier = Modifier
                    .clip(shape = RoundedCornerShape(50))
                    .aspectRatio(1f)
                    .clickable { appViewModel.signOut() },
                )
                {
                    Icon(
                        painter = painterResource(id = R.drawable.logout_svgrepo_com),
                        contentDescription = "logout icon", modifier = Modifier
                            .padding(18.dp)
                            .clip(shape = RoundedCornerShape(50))
                    )
                }
            }
        }
    }
}



@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenPreview()
{
    ProjectCosmicHamsterTheme {
        /*HomeScreen()*/
    }
}