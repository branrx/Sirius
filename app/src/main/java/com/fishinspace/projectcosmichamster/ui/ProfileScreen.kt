package com.fishinspace.projectcosmichamster.ui

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ComponentActivity
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController
import java.util.Locale
import java.util.concurrent.Executors

var cardColorDark = Color(30, 30,34)

var fieldNames = listOf<String>("username", "name", "surname", "age", "gender",
    "major", "school", "year", "bio")

var fillFieldNames = listOf<String>("username", "name", "surname", "age", "gender",
"major", "school", "year", "bio", "picture")

var fieldEntry = mutableMapOf<String, String>("username" to "", "name" to "", "surname" to "", "age" to "", "gender" to "",
    "major" to "", "school" to "", "year" to "", "bio" to "")

var editField = mutableStateOf("")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen()
{
    //  Dropdown variable holder
    var dropDownVar by remember { mutableStateOf("---")}
    val context = LocalContext.current

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
            uri: Uri? -> imageUri = uri;appViewModel.imgUri=uri;appViewModel.previewProfilePicture(context);editPicture = !editPicture
    }
    val screenheight = LocalConfiguration.current.screenHeightDp
    Log.d("uri", fieldEntry["username"].toString())

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        androidx.compose.animation.AnimatedVisibility(visible = true/*!alpha2 && !alpha3*/, exit = fadeOut(tween(250)),
            enter = fadeIn(tween(250)), modifier = Modifier
                .height((0.07f * screenheight).dp)
        )
        {
            //  top bar
            Row(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth())
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
                            Surface(modifier = Modifier
                                .clickable {
                                    //  Takes user to previous window when profile was opened
                                    when(appViewModel.activeWindow)
                                    {
                                        0 -> navController.navigate(Destination.MessagesScreen.route)
                                        1 -> navController.navigate(Destination.ExploreScreen.route)
                                        2 -> navController.navigate(Destination.PostScreen.route)
                                        3 -> navController.navigate(Destination.RequestsScreen.route)
                                    } }
                            )
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_left_svgrepo_com),
                                    contentDescription = "back icon", modifier = Modifier.padding(top = 18.dp, bottom = 18.dp, start = 12.dp),
                                )
                            }
                            Text(
                                "Profile", fontFamily = bison, fontWeight = FontWeight.Bold, letterSpacing = 2.sp,
                                fontSize = 28.sp, modifier = Modifier.padding(start=10.dp))
                        }

                        //  menu option
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
                                .clickable { appViewModel.toggleMainMenu() },
                            )
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.dots_three_vertical_fill_svgrepo_com),
                                    contentDescription = "menu option", modifier = Modifier
                                        .padding(16.dp)
                                        .clip(shape = RoundedCornerShape(50))
                                )
                            }
                        }
                    }
                }
            }
        }



        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .height((0.3f * screenheight).dp)
                .fillMaxSize())
        {
            Box(modifier = Modifier.fillMaxSize())
            {
                //  Holds the backdrop
                Surface(modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                {
                    Image(painter = painterResource(id = R.drawable.backdrop_v2), contentDescription = "backdrop",
                        modifier = Modifier,
                        contentScale = ContentScale.FillWidth)
                }
                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Column(modifier = Modifier
                        .weight(0.8f)
                        .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center)
                    {
                        profilePictureComposable(galleryPicker, onClick = { galleryPicker.launch("image/*")})
                    }

                    Row(horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(0.2f)
                            .fillMaxSize())
                    {
                        Surface(shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
                                .clickable { /**/ },
                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp).copy(0.3f)
                                )
                        {
                            Row(horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                                top = 0.dp,
                                                bottom = 0.dp
                                            ))
                            {
                                Icon(painter = painterResource(id = R.drawable.camera_svgrepo_com__1_), contentDescription = "edit icon",
                                    modifier = Modifier
                                        .alpha(1f)
                                        .padding(12.dp)
                                )
                                //  Displays the users name or tag
                                Text(
                                    "Change Picture", fontFamily = bison, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp,
                                    fontSize = 18.sp, modifier = Modifier.padding(start=0.dp), color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
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

        //  information list
        Column(modifier = Modifier
            //.verticalScroll(rememberScrollState(), enabled = true)
            .padding(top = 0.dp),
            verticalArrangement = Arrangement.Center)
        {
            Box(modifier = Modifier)
            {
                //chatsComposable()
                androidx.compose.animation.AnimatedVisibility(visible = !alpha2 && !alpha3, exit = fadeOut(tween(250, easing = LinearEasing)),
                    enter = fadeIn(tween(250, easing = LinearEasing, delayMillis = 0))
                )
                {
                    LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = Modifier.alpha(alpha1))
                    {
                        item{
                            //  profile picture composable

                        }
                        items(fieldNames.size)
                        {
                                item -> var temp by remember{ mutableStateOf("")};
                            profileInfoComposable(fieldNames[item], Modifier.fillMaxHeight(0.2f),
                                value = temp, onChange={temp=it}, index = item, onClick = {editMode = !editMode; editField.value = it})
                        }

                        item{
                            Column(horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .padding(top = 12.dp, bottom = 4.dp)
                                    .fillMaxWidth()
                                //.background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                            )
                            {
                                Surface(color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp).copy(0.4f),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
                                )
                                {
                                    Button(onClick = { appViewModel.saveEdits() },modifier = Modifier
                                        .alpha(alpha1)
                                        .fillMaxWidth()
                                        .padding(start = 4.dp, end = 4.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.surface.copy(0.5f))
                                    )
                                    {
                                        Text(text = "Save Change", modifier = Modifier, fontFamily = bison,
                                            fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp, fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                androidx.compose.animation.AnimatedVisibility(visible = alpha2, exit = fadeOut(tween(250, easing = LinearEasing)),
                    enter = fadeIn(tween(250, easing = LinearEasing, delayMillis = 200))
                )
                {
                    //  edit mode text
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(1f)
                            .padding(bottom = 8.dp))
                    {
                        //Spacer(modifier = Modifier.weight(0.5f))
                        Text(text = editField.value, modifier = Modifier.padding(bottom = 16.dp),
                            fontFamily = bison, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp,
                            fontSize = 18.sp)

                        AnimatedVisibility(visible = editField.value!="school" && editField.value != "year"
                                && editField.value != "gender")
                        {
                            TextInputComposable(value = value, onChange = {value = it})
                        }

                        AnimatedVisibility(visible = editField.value=="school" || editField.value == "year"
                                || editField.value == "gender")
                        {
                            DropSelectMenu(onSelect = { dropDownVar = it; value = it}, dropDownVar = dropDownVar,
                                fieldName = editField.value)
                        }

                        //Spacer(modifier = Modifier.weight(0.9f))

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

        androidx.compose.animation.AnimatedVisibility(visible = alpha2, exit = slideOutHorizontally(tween(200)),
            enter = slideInHorizontally(tween(200, delayMillis = 200)), modifier = Modifier.height((0.07f * screenheight).dp)
        ){
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
                        dropDownVar = "---"
                        editMode = !editMode;
                        if(alpha2){appViewModel.setEdit(editField.value, value)}
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
}

@Composable
fun TextInputComposable(value: String, onChange: (String)->Unit)
{
    val onSurface = MaterialTheme.colorScheme.onSurface
    Row(modifier = Modifier
        .fillMaxWidth(0.7f)
        .padding(top = 16.dp)
        , verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center)
    {
        Surface(modifier = Modifier,
            color = MaterialTheme.colorScheme.outlineVariant,
            shape = RoundedCornerShape(8.dp)
        )
        {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(0.8f),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                BasicTextField(value = value, onValueChange = onChange, modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center,
                        fontFamily = yanone, fontSize = 18.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Normal),
                    cursorBrush = Brush.sweepGradient(listOf(onSurface, onSurface)),
                    singleLine = true
                )
            }
        }
    }
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
        ElevatedCard(shape = RoundedCornerShape(100), modifier = Modifier
            .padding(2.dp)
            .align(Alignment.Center)
            .clip(shape = RoundedCornerShape(50))
            .clipToBounds()
            .requiredSize(140.dp)
            .clipToBounds()
            .border(
                2.dp,
                color = MaterialTheme.colorScheme.surface.copy(0.5f),
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
                contentScale = ContentScale.Crop,
            )
        }
        Row(modifier = Modifier
            .clickable(onClick = onClick, enabled = appViewModel.dbAuth.currentUser != null)
            .clip(shape = RoundedCornerShape(50)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        )
        {

        }
    }
}

//  .clickable { galleryPicker.launch("image/*") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun profileInfoComposable(fieldName: String, modifier: Modifier, value: String, onChange: (String)->Unit, index: Int,
                          onClick: (String) -> Unit)
{
    val screenheight = LocalConfiguration.current.screenHeightDp
    var fieldText = appViewModel.getField(fieldName)
    Surface(modifier = Modifier
        .height((0.10f * screenheight).dp)
        .fillMaxSize()
        .padding(top = 4.dp, start = 4.dp, end = 4.dp)
        .clickable { onClick(fieldName) },
        shape = RoundedCornerShape(20),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp).copy(0.1f)
    )
    {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            //.fillMaxSize().border(0.dp, color = brown, shape = RoundedCornerShape(12.dp))
        )
        {
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier
                .weight(0.7f)
                .fillMaxSize()
                .background(Color.Transparent))
            {
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier
                    .weight(0.4f)
                    .padding(start = 16.dp)
                    .fillMaxSize()
                    .fillMaxHeight())
                {
                    Text(text = fieldName.uppercase(Locale.getDefault()), modifier = Modifier
                        .padding(start = 8.dp)
                        .alpha(1f),
                        fontFamily = bison, fontWeight = FontWeight.Normal, letterSpacing = 2.sp,
                        fontSize = 16.sp
                    )
                    //Spacer(modifier = Modifier.weight(0.5f))
                }

                //Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(start = 16.dp, end = 40.dp, top = 4.dp))

                Column(verticalArrangement = Arrangement.Top, modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(0.4f)
                    .fillMaxSize())
                {
                    Text(text = fieldText, modifier = Modifier
                        .alpha(0.9f)
                        .padding(start = 8.dp)
                        .alpha(0.6f),
                        fontFamily = yanone, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp,
                        fontSize = 22.sp
                    )
                }
            }

            Surface(modifier = Modifier
                .weight(0.3f)
                .fillMaxSize(),
                color = MaterialTheme.colorScheme.surface.copy(0.5f))
            {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                )
                {
                    Text(text = "edit", modifier = Modifier
                        .padding(end = 0.dp),
                        fontFamily = bison, fontWeight = FontWeight.SemiBold, letterSpacing = 4.sp,
                        fontSize = 16.sp,
                        color =  MaterialTheme.colorScheme.onSurface
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

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun ProfileScreenPreview()
{
    MaterialTheme {
        ProfileScreen()
    }
}