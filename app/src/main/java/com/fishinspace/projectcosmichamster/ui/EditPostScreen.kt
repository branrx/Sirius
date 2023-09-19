package com.fishinspace.projectcosmichamster.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.activityContext
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostScreen()
{
    var postCategory by remember { mutableStateOf(appViewModel.postEditCategory) }
    var postCategoryDescription by remember { mutableStateOf(appViewModel.postEditSpecifier) }
    var postDescription by remember { mutableStateOf(appViewModel.postEditNote) }

    var selectedCat by remember { mutableStateOf(catOptions.indexOf(postCategory)) }
    var selectedSpe by remember { mutableStateOf(specificOptions[selectedCat]?.indexOf(postCategoryDescription)) }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Spacer(modifier = Modifier.weight(0.05f))

        //Text(text = "Post To Board", fontFamily = bison,
        //    fontWeight = FontWeight.SemiBold, fontSize = 36.sp, letterSpacing = 1.sp)
        //Spacer(modifier = Modifier.weight(0.1f))

        //  category options composable
        Column(modifier = Modifier
            .requiredHeight(80.dp)
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

        Spacer(modifier = Modifier.weight(0.02f))

        //  category specific composable
        Column(modifier = Modifier
            .requiredHeight(72.dp)
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
                        item ->  catSpecificComposable(item, onClick = { selectedSpe = it}, selectedSpe!!.toInt(), selectedCat)
                }
            }
        }

        Text(text = "Add a note", fontFamily = bison,
            fontWeight = FontWeight.SemiBold, fontSize = 22.sp, letterSpacing = 1.sp,
            modifier = Modifier.padding(top = 20.dp)
        )

        //  assists
        Row(modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(0.8f))
        {
            Text(text = "e.g Preferrably someone who can cook, cause you can't study on an empty stomach.", fontFamily = ebrima,
                fontWeight = FontWeight.Normal, fontSize = 16.sp, letterSpacing = 0.sp,
                modifier = Modifier.alpha(0.5f), textAlign = TextAlign.Center
            )
        }

        //  input
        Row(modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(0.9f))
        {
            postInputComposable(postDescription, onChange = { postDescription = it})
        }

        Row(modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(0.6f))
        {
            Text(text = "Posts last for 24 hours, everyone inside your major zone can see and reply to them (friend or not).", fontFamily = bison,
                fontWeight = FontWeight.Normal, fontSize = 16.sp, letterSpacing = 1.sp,
                modifier = Modifier.alpha(0.4f), textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(0.08f))

        //  BUTTONS
        Row(modifier = Modifier.fillMaxWidth(0.8f))
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
                    appViewModel.postToBoard(catOptions[selectedCat], specificOptions[selectedCat]!![selectedSpe!!.toInt()], postDescription)
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
                Text("Re-Post", fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }
        }
    }
}

