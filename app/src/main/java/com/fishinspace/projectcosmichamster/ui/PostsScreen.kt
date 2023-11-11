package com.fishinspace.projectcosmichamster.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.delay

@Composable
fun PostsScreen()
{
    val screenheight = LocalConfiguration.current.screenHeightDp
    var filterBy by remember { mutableStateOf("none") }
    val postsList by appViewModel.postsList.collectAsState()
    val showMainMenu by appViewModel.uiState.collectAsState()
    Log.d("posts count", postsList.postsCount.toString())
    var isPostsLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = appViewModel.timerPostsLoading)
    {
        if(appViewModel.isPostsLoading)
        {
            isPostsLoading = true
            delay(4000)
            isPostsLoading = false
            appViewModel.isPostsLoading = false
        }
    }

    Box()
    {
        Column(modifier = Modifier.fillMaxSize())
        {
            //  top bar
            Row(modifier = Modifier
                .height((0.07f * screenheight).dp)
                .fillMaxWidth())
            {
                topBarComposable()
            }

            //  Holds posts, filters... spans entire screen
            //  Main Screen, spans entire screen
            Box(modifier = Modifier
                .weight(0.9f)
            )
            {
                Column()
                {
                    //  current screen indicator
                    Row(modifier = Modifier
                        .requiredHeight(40.dp)
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Surface(modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .fillMaxWidth(0.35f)
                            .padding(8.dp), shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.inversePrimary))),
                            color = MaterialTheme.colorScheme.background
                        )
                        {
                            Row(modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center)
                            {
                                Text(text = "Board", fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = 1.sp)
                            }
                        }
                    }

                    //  Holds the filters
                    AnimatedVisibility(visible = postsList.postsCount>1,
                        modifier = Modifier
                            .weight(0.15f))
                    {
                        Row(modifier = Modifier
                            .fillMaxSize()
                        )
                        {
                            BoardFiltersComposable()
                        }
                    }

                    //  Holds the posts
                    //  user profile
                    AnimatedVisibility(visible = postsList.postsCount>1,
                        modifier = Modifier.weight(0.9f))
                    {
                        Column(modifier = Modifier
                            //.fillMaxSize()
                            .weight(1f)
                            .padding(0.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        )
                        {
                            LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = Modifier.fillMaxSize())
                            {
                                var posts: List<PostClass> = listOf()

                                posts = if(filterBy!="none")
                                {
                                    FilterBy(posts = postsList.postsList, filterBy = filterBy)
                                }   else {
                                    postsList.postsList
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

                    //  If no posts are available
                    AnimatedVisibility(visible = postsList.postsCount<1,
                        modifier = Modifier.weight(0.9f))
                    {
                        Column(modifier = Modifier
                            .weight(0.9f)
                            .fillMaxSize(), verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally)
                        {
                            NoEntryComposable("Posts")
                        }
                    }
                }

                //  Create post button
                Column(modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                    //.requiredHeight(60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Surface(shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .requiredSize(52.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(50))
                            .clickable { navController.navigate(Destination.CreatePostScreen.route) },
                        color = MaterialTheme.colorScheme.secondary,
                        border = BorderStroke(2.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondary)))
                    )
                    {
                        Column()
                        {
                            Icon(painter = painterResource(id = R.drawable.notes_svgrepo_com),
                                contentDescription = "add post button",
                                modifier = Modifier.padding(12.dp),
                            )
                        }
                    }
                }

                //  Loading icon
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center)
                {
                    AnimatedVisibility(visible = isPostsLoading, exit = fadeOut(tween(500)),
                        enter = fadeIn(tween(500)))
                    {
                        LoadingOverlay()
                    }
                }

            }

            //  navigation bar
            Column(modifier = Modifier
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
                        Text(text = getTime(post.time.toString()), modifier = Modifier.padding(2.dp),
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

@Composable
fun BoardFiltersComposable()
{
    var filterBy by remember { mutableStateOf("none") }

    Surface(modifier = Modifier
        .padding(start = 4.dp, end = 4.dp),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp).copy(0.2f),
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
                        item ->  FilterComposable(item,
                    onClick = { filterBy = if(filterBy==it){ "none" }else{ it } }, filterBy)
                }
            }
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
        .requiredHeight(32.dp)
        .clip(RoundedCornerShape(50))
        .clickable { onClick(filter) },
        shape = RoundedCornerShape(100),
        border = BorderStroke(1.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary))),
        color = if(filterBy==filter){MaterialTheme.colorScheme.secondary}else{MaterialTheme.colorScheme.surface}
    )
    {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 12.dp, end = 12.dp))
        {
            Text(text = filter, modifier = Modifier.padding(),
                fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = 1.sp)
        }
    }
}