package com.fishinspace.projectcosmichamster.ui

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInExpo
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import java.util.Locale
import java.util.concurrent.Executors

var cardColorDark = Color(30, 30,34)

var fieldNames = listOf<String>("username", "name", "surname", "age", "gender",
"major", "school", "year", "bio")

var fieldEntry = mutableMapOf<String, String>("username" to "", "name" to "", "surname" to "", "age" to "", "gender" to "",
    "major" to "", "school" to "", "year" to "", "bio" to "")

var editField = mutableStateOf("")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen()
{

    var editMode by remember{ mutableStateOf(false)}
    var editPicture by remember{ mutableStateOf(false)}
    var value by remember { mutableStateOf("")}

    var alpha1 by remember{ mutableStateOf(1f)}
    var alpha2 by remember{ mutableStateOf(false)}
    var alpha3 by remember{ mutableStateOf(false)}

    if(editPicture)
    {
        alpha3 = true
        alpha1 = 0f
        alpha2 = false
    }else{
        alpha3 = false
        alpha1 = 1f
        alpha2 = false
    }

    if(editMode && !editPicture)
    {
        alpha1 = 0f
        alpha2 = true
    }   else if(!editMode && !editPicture){
        alpha1 = 1f
        alpha2 = false
    }

    //  contract activity for picking items from gallery
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val galleryPicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent())
    {
            uri: Uri? -> imageUri = uri;appViewModel.imgUri=uri;appViewModel.previewProfilePicture();editPicture = !editPicture
    }

    Log.d("uri", fieldEntry["username"].toString())

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface))
    {
        androidx.compose.animation.AnimatedVisibility(visible = !alpha2 && !alpha3, exit = fadeOut(tween(250)),
            enter = slideInVertically(tween(250))
        )
        {
            //  top bar
            Row(modifier = Modifier
                .requiredHeight(60.dp)
                .fillMaxWidth())
            {
                topBarComposable()
            }
        }

        androidx.compose.animation.AnimatedVisibility(visible = !alpha2 && !alpha3, exit = fadeOut(tween(200)),
            enter = fadeIn(tween(200))
        )
        {
            Row(modifier = Modifier
                .requiredHeight(40.dp)
                .animateContentSize()
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
                        Text(text = "PROFILE", fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = 1.sp)
                    }
                }
            }
        }


        /*Button(onClick = { appViewModel.uploadProfile() }, modifier = Modifier.weight(0.1f)) {
            Text("Upload")
        }
        Button(onClick = { /*appViewModel.saveProfileDisk()*/ }, modifier = Modifier.weight(0.1f)) {
            Text("getProPic")
        }*/

        //  messages list
        Column(modifier = Modifier
            .weight(0.7f)
            .animateContentSize()
            .padding(top = 0.dp))
        {
            Box(modifier = Modifier)
            {
                //chatsComposable()
                LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = Modifier.alpha(alpha1))
                {
                    item{
                        //  profile picture composable
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .requiredHeight(120.dp)
                                .fillMaxSize())
                        {
                            profilePictureComposable(galleryPicker, onClick = { galleryPicker.launch("image/*")})
                        }
                    }
                    items(fieldNames.size)
                    {
                            item -> var temp by remember{ mutableStateOf("")};profileInfoComposable(fieldNames[item], Modifier.fillMaxHeight(0.1f),
                        value = temp, onChange={temp=it}, index = item, onClick = {editMode = !editMode; editField.value = it})
                    }

                    item{
                        //  profile picture composable
                        Spacer(modifier = Modifier.requiredHeight(62.dp))
                    }
                }

                ElevatedButton(onClick = { appViewModel.saveEdits() },modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.6f)
                    .alpha(alpha1)
                    .padding(bottom = 4.dp, end = 16.dp)
                )
                {
                    Text(text = "Save", modifier = Modifier, fontFamily = bison,
                        fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp, fontSize = 16.sp
                    )
                }

                androidx.compose.animation.AnimatedVisibility(visible = alpha2, exit = fadeOut(tween(250, easing = LinearEasing)),
                    enter = fadeIn(tween(250, easing = LinearEasing))
                )
                {
                    //  edit mode text
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(1f)
                            .padding(bottom = 8.dp))
                    {
                        Surface(modifier = Modifier
                            .fillMaxWidth(0.35f)
                            .padding(8.dp), shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.inversePrimary)))
                        )
                        {
                            Row(modifier = Modifier
                                .padding(4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center)
                            {
                                Text(text = "PROFILE", fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = 1.sp)
                            }
                        }
                        Spacer(modifier = Modifier.weight(0.5f))

                        Text(text = editField.value, modifier = Modifier.padding(bottom = 0.dp),
                            fontFamily = bison, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp,
                            fontSize = 18.sp)

                        Column(modifier = Modifier
                            .padding(top = 12.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .requiredWidth(300.dp)
                            .requiredHeight(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center)
                        {
                            BasicTextField(value = value, onValueChange = {value = it}, modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center),
                                cursorBrush = Brush.sweepGradient(listOf(Color.White, Color.White))
                            )
                        }

                        Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.requiredWidth(300.dp))

                        Spacer(modifier = Modifier.weight(0.9f))

                        Row(modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically)
                        {
                            ElevatedButton(onClick = {
                                if(value.isNotEmpty())
                                {
                                    value = ""
                                }
                                editMode = !editMode
                            }, modifier = Modifier
                                .weight(0.4f)
                                .padding(end = 2.dp)
                                .padding(bottom = 0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
                            )
                            {
                                Text("Cancel", fontFamily = bison,
                                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
                            }

                            ElevatedButton(onClick = {
                                if(value.isNotEmpty())
                                {
                                    editMode = !editMode; if(alpha2){appViewModel.setEdit(editField.value, value)}
                                    value = ""
                                }
                            }, modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .padding(start = 2.dp)
                                .padding(bottom = 0.dp),
                            )
                            {
                                Text("Done", fontFamily = bison,
                                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
                            }
                        }

                    }
                }

                androidx.compose.animation.AnimatedVisibility(visible = alpha3, exit = fadeOut(tween(250, easing = EaseOutExpo)),
                    enter = fadeIn(tween(250, easing = EaseInExpo))
                    )
                {
                    //  edit mode text
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(1f)
                            .padding(bottom = 8.dp))
                    {
                        Surface(modifier = Modifier
                            .fillMaxWidth(0.35f)
                            .padding(8.dp), shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, brush = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.inversePrimary)))
                        )
                        {
                            Row(modifier = Modifier
                                .padding(4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center)
                            {
                                Text(text = "PROFILE", fontFamily = bison, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, letterSpacing = 1.sp)
                            }
                        }

                        Spacer(modifier = Modifier.weight(0.5f))

                        Card(modifier = Modifier
                            .aspectRatio(1f)
                            .padding(12.dp))
                        {
                            Image(bitmap = appViewModel.userProfilePicture, contentDescription = "profile picture preview",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize())
                        }

                        Spacer(modifier = Modifier.weight(0.9f))

                        Row(modifier = Modifier
                            .padding(top = 0.dp)
                            .fillMaxWidth(0.7f), horizontalArrangement = Arrangement.End)
                        {
                            ElevatedButton(onClick = { appViewModel.cancelProfileChange(); editPicture = !editPicture },
                                modifier = Modifier
                                    .weight(0.4f)
                                    .padding(end = 2.dp)
                                    .padding(bottom = 0.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
                            )
                            {
                                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically)
                                {
                                    Text("Cancel", fontFamily = bison,
                                        fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
                                }
                            }

                            ElevatedButton(onClick = {
                                                     Executors.newSingleThreadExecutor().execute {
                                                         appViewModel.uploadProfile()
                                                         editPicture = !editPicture
                                                     }
                            },
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .padding(start = 2.dp)
                                    .padding(bottom = 0.dp),
                            )
                            {
                                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically)
                                {
                                    Text("Done", fontFamily = bison,
                                        fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }
                /*//  shows a loading animation
                Column(modifier = Modifier.align(Alignment.Center))
                {
                    loadingOverlay()
                }*/
            }
        }

        //  save button
        /*Row(modifier = Modifier
            .weight(0.15f)
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .background(cardColorDark), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically)
        {
            saveButtonComposable()
        }*/

        androidx.compose.animation.AnimatedVisibility(visible = !alpha2 && !alpha3, exit = fadeOut(tween(250)),
            enter = slideInVertically(tween(250))
        )
        {
            //  navigation bar
            Row(modifier = Modifier
                .requiredHeight(60.dp)
                .fillMaxWidth())
            {
                navigationComposable()
            }
        }
    }
}

@Composable
fun loadingOverlay()
{
    var transition = rememberInfiniteTransition()

    var rotationAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing))
    )

    Surface(shape = RoundedCornerShape(50),
        modifier = Modifier.aspectRatio(1f).requiredSize(40.dp)
            .rotate(rotationAnimation.value),
        border = BorderStroke(4.dp,
            brush = Brush.sweepGradient(listOf(MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.inversePrimary))),
        color = Color.Transparent
    )
    {}
}

@Composable
fun profilePictureComposable(galleryPicker: ManagedActivityResultLauncher<String, Uri?>, onClick: ()->Unit)
{
    var isUploading by remember { mutableStateOf(appViewModel.isUploading)}

    var transition = rememberInfiniteTransition()

    var rotationAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing))
    )

    Box(modifier = Modifier)
    {
        Card(shape = RoundedCornerShape(100), modifier = Modifier
            .padding(2.dp)
            .align(Alignment.Center)
            .clip(shape = RoundedCornerShape(50))
            .requiredSize(92.dp)
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.inversePrimary,
                shape = RoundedCornerShape(50)
            ),
            colors =  CardDefaults.cardColors(containerColor = Color.Transparent))
        {
            Image(appViewModel.userProfilePicture,
                contentDescription = "user profile picture",
                modifier = Modifier
                    .scale(1f)
                    .clip(shape = RoundedCornerShape(50))
                    .clickable {
                        galleryPicker.launch("image/*")
                    },
                contentScale = ContentScale.Crop
            )
        }
        Row(modifier = Modifier
            .clickable(onClick = onClick, enabled = appViewModel.dbAuth.currentUser != null)
            .clip(shape = RoundedCornerShape(50)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        )
        {
            if(isUploading)
            {
                Icon(painter = painterResource(id = R.drawable.camera_svgrepo_com__1_), contentDescription = "edit icon",
                    modifier = Modifier
                        .scale(0.2f)
                        .alpha(0.7f)
                        .rotate(rotationAnimation.value),
                    tint = MaterialTheme.colorScheme.primary
                )
            }   else{
                Icon(painter = painterResource(id = R.drawable.camera_svgrepo_com__1_), contentDescription = "edit icon",
                    modifier = Modifier
                        .scale(0.2f)
                        .alpha(0.7f),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

//  .clickable { galleryPicker.launch("image/*") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun profileInfoComposable(fieldName: String, modifier: Modifier, value: String, onChange: (String)->Unit, index: Int,
                          onClick: (String) -> Unit)
{
    var fieldText = appViewModel.getField(fieldName)
    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(top = 8.dp, start = 8.dp, end = 8.dp)
        .requiredHeight(60.dp),
        shape = RoundedCornerShape(20),
        color = MaterialTheme.colorScheme.surface
    )
    {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            //.fillMaxSize().border(0.dp, color = brown, shape = RoundedCornerShape(12.dp))
        )
        {
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .weight(0.7f))
            {
                Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .weight(0.4f))
                {
                    Text(text = fieldName.uppercase(Locale.getDefault()), modifier = Modifier.padding(start = 16.dp),
                        fontFamily = yanone, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp,
                        fontSize = 16.sp
                    )
                }

                Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(start = 16.dp, end = 40.dp, top = 4.dp))

                Column(verticalArrangement = Arrangement.Center, modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .weight(0.6f))
                {
                    Text(text = fieldText, modifier = Modifier.padding(start = 16.dp),
                        fontFamily = yanone, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp
                    )
                }

            }

            /*Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxSize()
                .weight(0.5f),
                horizontalArrangement = Arrangement.Start
            )
            {
                Text(text = fieldText, modifier = Modifier.padding(start = 12.dp),
                    fontFamily = bison, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp
                )
            }*/

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxSize()
                .weight(0.2f),
                horizontalArrangement = Arrangement.End
            )
            {
                ElevatedButton(onClick = { onClick(fieldName) },
                    shape = RoundedCornerShape(topStartPercent = 16, topEndPercent = 50, bottomStartPercent = 16, bottomEndPercent = 50))
                {
                    Text(text = "|edit", modifier = Modifier
                        .padding(end = 0.dp),
                        fontFamily = bison, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp
                    )
                }
            }
        }

    }
    //Divider(thickness = Dp.Hairline, color = Color.White, modifier = Modifier.padding(start = 32.dp, end = 32.dp))
}

@Composable
fun saveButtonComposable()
{
    ElevatedButton(onClick = { appViewModel.saveEdits() }, modifier = Modifier
        .fillMaxWidth(0.4f)
        .padding(end = 0.dp),
    )
    {
        Text(text = "Save", modifier = Modifier, fontFamily = bison,
            fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp)
    }
}

fun saveProfileInfo()
{
    var userid = appViewModel.dbAuth.currentUser!!.uid
    var ref = appViewModel.dbObject.reference.child("users").child(userid)
    fieldEntry.forEach {
        ref.child(it.key).setValue(it.value)
    }
}