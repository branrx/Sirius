package com.fishinspace.projectcosmichamster.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.MainActivity
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.activityContext
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController

val test = listOf("A Roommate", "A House", "A Party", "Some Food", "A Man", "A Job")

//  cat options
val catOptions = listOf("accommodation", "academic", "miscellaneous", "buy/sell")

//  cat specific options
val specificOptions = mapOf(0 to listOf("a roomate", "a house"),
    1 to listOf("a study buddy", "a group partner"),
    2 to listOf("event: group and chill"),
    3 to listOf("selling", "buying")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen()
{
    var postCategory by remember { mutableStateOf("") }
    var postCategoryDescription by remember { mutableStateOf("") }
    var postDescription by remember { mutableStateOf("") }

    var selectedCat by remember { mutableStateOf(0) }
    var selectedSpe by remember { mutableStateOf(0) }

    val screenheight = LocalConfiguration.current.screenHeightDp

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        //  category options composable
        Row(modifier = Modifier
            .height((0.08f * screenheight).dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically)
        {
            Icon(painter = painterResource(id = R.drawable.arrow_left_svgrepo_com), contentDescription = "home",
                modifier = Modifier.padding(top = 18.dp, bottom = 18.dp, start = 8.dp)
                    .clipToBounds().rotate(0f))
            Text(text = "Post to Board", fontFamily = bison, fontWeight = FontWeight.Bold, letterSpacing = 2.sp,
                fontSize = 28.sp, modifier = Modifier.padding(start=10.dp)
            )
        }

        Spacer(modifier = Modifier.height((0.05f * screenheight).dp))

        //Text(text = "Post To Board", fontFamily = bison,
        //    fontWeight = FontWeight.SemiBold, fontSize = 36.sp, letterSpacing = 1.sp)
        //Spacer(modifier = Modifier.weight(0.1f))

        //  category options composable
        Column(modifier = Modifier
            .height((0.1f * screenheight).dp)
            .fillMaxWidth())
        {
            Row(modifier = Modifier.weight(0.4f).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween)
            {
                Text(text = "Select a Category", fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, fontSize = 22.sp, letterSpacing = 1.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Icon(painter = painterResource(id = R.drawable.arrow_left_svgrepo_com), contentDescription = "home",
                    modifier = Modifier.padding(6.dp).clipToBounds().rotate(-180f))
            }

            LazyHorizontalGrid(rows = GridCells.Fixed(1), modifier = Modifier.weight(0.6f))
            {
                items(4)
                {
                    item ->  catOptionsComposable(item, onClick = { selectedCat = it; selectedSpe = 0}, selectedCat)
                }
            }
        }

        //  category specific composable
        Column(modifier = Modifier
            .height((0.1f * screenheight).dp)
            .fillMaxWidth())
        {
            Text(text = "Looking for?", fontFamily = bison,
                fontWeight = FontWeight.SemiBold, fontSize = 22.sp, letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 8.dp)
            )

            LazyHorizontalGrid(rows = GridCells.Fixed(1))
            {
                items(specificOptions[selectedCat]!!.size)
                {
                        item ->  catSpecificComposable(item, onClick = { selectedSpe = it}, selectedSpe, selectedCat)
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.08f))


        Row(modifier = Modifier
            .padding(4.dp)
            .height((0.1f * screenheight).dp)
            .fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom)
        {
            Text(text = "Add a note", fontFamily = bison,
                fontWeight = FontWeight.SemiBold, fontSize = 22.sp, letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 20.dp)
            )
        }

        //  assists
        Row(modifier = Modifier
            .padding(4.dp)
            .height((0.1f * screenheight).dp)
            .fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically)
        {
            Text(text = "e.g Preferrably someone who can cook, cause you can't study on an empty stomach.", fontFamily = ebrima,
                fontWeight = FontWeight.Normal, fontSize = 16.sp, letterSpacing = 0.sp,
                modifier = Modifier.alpha(0.5f), textAlign = TextAlign.Center
            )
        }

        //  input
        Row(modifier = Modifier
            .padding(top = 16.dp)
            .height((0.1f * screenheight).dp)
            .fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically)
        {
            postInputComposable(postDescription, onChange = { postDescription = it})
        }

        Row(modifier = Modifier
            .padding(4.dp)
            .height((0.1f * screenheight).dp)
            .fillMaxWidth(0.6f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically)
        {
            Text(text = "Posts last for 24 hours, everyone inside your major zone can see and reply to them (friend or not).", fontFamily = bison,
                fontWeight = FontWeight.Normal, fontSize = 16.sp, letterSpacing = 1.sp,
                modifier = Modifier.alpha(0.4f), textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(0.08f))

        //  BUTTONS
        Row(modifier = Modifier.fillMaxWidth(0.8f)
            .height((0.17f * screenheight).dp),
            verticalAlignment = Alignment.Bottom)
        {
            ElevatedButton(onClick = {
                navController.navigate(Destination.ExploreScreen.route)
                if(postDescription.isNotEmpty())
                {
                    postDescription = ""
                }
            }, modifier = Modifier
                .fillMaxWidth(0.7f)
                .weight(0.5f).imePadding()
                .padding(end = 2.dp)
                .padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
            )
            {
                Text("Cancel", fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }

            ElevatedButton(onClick = {
                if(postDescription.isNotEmpty())
                {
                    //  passes the cat, specifier and note
                    appViewModel.postToBoard(catOptions[selectedCat], specificOptions[selectedCat]!![selectedSpe], postDescription)
                    navController.navigate(Destination.ExploreScreen.route)
                }else{
                    Toast.makeText(activityContext ,"Post message cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier
                .weight(0.7f)
                .padding(start = 2.dp)
                .padding(bottom = 8.dp),
            )
            {
                Text("Post", fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun catOptionsComposable(index: Int, onClick: (Int)->Unit, selectedCat: Int)
{
    Log.d("selected cat", selectedCat.toString())
    //  get cat icon painter
    var iconResource: Int = 0
    when(index)
    {
        0 -> { iconResource = R.drawable.home_svgrepo_com__1_ }
        1 -> { iconResource = R.drawable.square_academic_cap_svgrepo_com }
        2 -> { iconResource = R.drawable.emoji_funny_square_svgrepo_com }
        3 -> { iconResource = R.drawable.cart_large_2_svgrepo_com }
        else -> { iconResource = R.drawable.home_svgrepo_com__1_ }
    }

    Surface(modifier = Modifier.padding(8.dp)
        .clip(RoundedCornerShape(30))
        .clickable{ onClick(index) },
        shape = RoundedCornerShape(30),
        border = BorderStroke(1.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.inversePrimary, MaterialTheme.colorScheme.inversePrimary))),
        color = if(selectedCat==index){MaterialTheme.colorScheme.inversePrimary}else{MaterialTheme.colorScheme.surface}
    )
    {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 6.dp))
        {
            Icon(painter = painterResource(id = iconResource), contentDescription = "home",
                modifier = Modifier.padding(6.dp))
            Text(text = catOptions[index], modifier = Modifier.padding(end = 12.dp),
                fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, letterSpacing = 1.sp)
        }
    }
}

@Composable
fun catSpecificComposable(index: Int, onClick: (Int)->Unit, selectedSpe: Int, selectedCat: Int)
{
    var options = listOf<String>()

    options = when(selectedCat)
    {
        0 -> {
            specificOptions[0]!!
        }
        1 -> {
            specificOptions[1]!!
        }
        2 -> {
            specificOptions[2]!!
        }
        3 -> {
            specificOptions[3]!!
        }
        else -> {
            specificOptions[0]!!
        }
    }

    Surface(modifier = Modifier.padding(8.dp)
        .clip(RoundedCornerShape(30))
        .clickable{ onClick(index) },
        shape = RoundedCornerShape(30),
        border = BorderStroke(1.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.inversePrimary, MaterialTheme.colorScheme.inversePrimary))),
        color = if(selectedSpe==index){MaterialTheme.colorScheme.inversePrimary}else{MaterialTheme.colorScheme.surface}
    )
    {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 12.dp, end = 12.dp))
        {
            Text(text = options[index], modifier = Modifier.padding(),
                fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, letterSpacing = 1.sp)
        }
    }
}

@Composable
fun postInputComposable(value: String, onChange: (String)->Unit)
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
                //Spacer(modifier = Modifier.weight(0.2f))
                Surface(color = Color.Transparent)
                {
                    BasicTextField(value = value, onValueChange = onChange, modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(bottom = 4.dp),
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center),
                        cursorBrush = Brush.sweepGradient(listOf(onSurface, onSurface)),
                        singleLine = false,
                        maxLines = 2
                    )
                }
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .alpha(0.5f)
                        .padding(top = 0.dp))
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CreatePostPreview()
{
    CreatePostScreen()
}

