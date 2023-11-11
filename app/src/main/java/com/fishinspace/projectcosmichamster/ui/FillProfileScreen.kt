package com.fishinspace.projectcosmichamster.ui

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController

@Composable
fun FillProfileScreen()
{
    //  Dropdown variable holder
    var dropDownVar by remember { mutableStateOf("---")}
    var context = LocalContext.current

    //  Field value: e.g field: age ,field value = 24
    var value by remember { mutableStateOf("")}

    //  Field index: translates to username, name, surname, age fields etc..
    var fieldIndex by remember { mutableIntStateOf(0) }

    //  contract activity for picking items from gallery
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val galleryPicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent())
    {
            uri: Uri? -> imageUri = uri;appViewModel.imgUri=uri;appViewModel.previewProfilePicture(context)
    }

    Column(modifier = Modifier
        .fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(16.dp)
            .weight(0.4f)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        )
        {
            Icon(
                painter = painterResource(id = R.drawable.cirqle_logo_3),
                contentDescription = "app icon", modifier = Modifier
                    .weight(0.5f)
                    .padding(24.dp)
                    .clipToBounds(),
                tint = MaterialTheme.colorScheme.secondary
            )

            Text(text = stringResource(id = R.string.app_name), modifier = Modifier
                .padding(top=12.dp), fontFamily = bison,
                fontWeight = FontWeight.Bold, letterSpacing = 1.sp, fontSize = 40.sp,
                color = MaterialTheme.colorScheme.secondary)
            Text(text = "Setting Up Profile", modifier = Modifier.alpha(0.8f)
                , fontFamily = bison,
                fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 24.sp)
        }

        Column(modifier = Modifier.weight(0.5f), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
        {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 16.dp))
            {
                Text(text = "choose a ${fillFieldNames[fieldIndex]}.", modifier = Modifier, fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }

            AnimatedVisibility(fillFieldNames[fieldIndex]!="picture" &&
                    (fillFieldNames[fieldIndex]!="school" && fillFieldNames[fieldIndex]!="year"
                            && fillFieldNames[fieldIndex]!="gender"))
            {
                //  Captures the field value, be it name, age, school etc...
                FieldComposable(value = value, onChange = { value = it})
            }

            //  Displays dropdown menu, for school, year, gender
            AnimatedVisibility(visible = fillFieldNames[fieldIndex]=="school" || fillFieldNames[fieldIndex] == "year"
                    || fillFieldNames[fieldIndex] == "gender")
            {
                DropSelectMenu(onSelect = { dropDownVar = it; value = it}, dropDownVar = dropDownVar,
                    fieldName = fillFieldNames[fieldIndex])
            }

            //  Displays profile selector, for profile picture
            AnimatedVisibility(fillFieldNames[fieldIndex]=="picture")
            {
                //  profile picture composable
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .requiredHeight(120.dp)
                        .fillMaxSize())
                {
                    FillProfilePicComposable(galleryPicker, onClick = { galleryPicker.launch("image/*")})
                }
            }

            //  Displays name of the field
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(top = 16.dp))
            {
                Text(text = fillFieldNames[fieldIndex], modifier = Modifier.alpha(0.5f), fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }
        }

        //  Holds navigation buttons, back and proceed
        Row(modifier = Modifier
            .fillMaxWidth(0.7f)
            .weight(0.2f), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center)
        {
            AnimatedVisibility(visible = fieldIndex > 0, modifier = Modifier.weight(0.4f))
            {
                ElevatedButton(onClick = {
                    //  Clear value var if user clicks back
                    if(fieldIndex>0)
                    {
                        value = ""
                        fieldIndex -= 1
                    }
                }, modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .weight(0.4f)
                    .padding(end = 2.dp)
                    .padding(bottom = 32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface)
                )
                {
                    Text("Back", fontFamily = bison,
                        fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
                }
            }

            ElevatedButton(onClick = {
                //  Move to next field only if value is not empty
                if(fillFieldNames[fieldIndex]!="picture")
                {
                    if(value.isEmpty())
                    {
                        Toast.makeText(context,"No ${fillFieldNames[fieldIndex]} entered.", Toast.LENGTH_SHORT).show()
                    } else{
                        appViewModel.setEdit(fillFieldNames[fieldIndex], value)
                        fieldIndex += 1
                        value = ""
                        dropDownVar = "---"
                    }
                } else if(fillFieldNames[fieldIndex]=="picture")
                {
                    if(imageUri!=null)
                    {
                        //  Upload user information
                        fillProfileUpload(context = context)
                        navController.navigate(Destination.ExploreScreen.route)
                    }else{
                        Toast.makeText(context,"No profile picture selected.", Toast.LENGTH_SHORT).show()
                    }
                }

            }, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(start = 2.dp)
                .padding(bottom = 32.dp),
            )
            {
                Text("Next", fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }
        }
    }
}

//  Drop down menu
@Composable
fun DropSelectMenu(onSelect: (String)->Unit, dropDownVar: String, fieldName: String)
{
    var isVisible by remember { mutableStateOf(false) }
    val options = when(fieldName)
    {
        "school" -> schools
        "year" -> years
        "gender" -> genders
        else -> listOf()
    }

    Column()
    {
        ElevatedButton(onClick = { isVisible = !isVisible },
            modifier = Modifier
                .requiredHeight(36.dp)
                .fillMaxWidth(0.4f),
            shape = RoundedCornerShape(8.dp)
        )
        {
            Row(modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(), verticalAlignment = Alignment.CenterVertically)
            {
                Column(modifier = Modifier
                    .weight(0.7f)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center)
                {
                    Text(dropDownVar, fontFamily = bison, fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp, fontSize = 16.sp)
                }
                Column(modifier = Modifier
                    .weight(0.3f)
                    .fillMaxSize(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center)
                {
                    Icon(painter = painterResource(id = R.drawable.arrow_down_svgrepo_com),
                        contentDescription = "dropdown menu",
                        modifier = Modifier
                            .clipToBounds()
                    )
                }
            }
        }

        DropdownMenu(expanded = isVisible, onDismissRequest = { isVisible = false },
            modifier = Modifier.fillMaxWidth(0.4f))
        {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(8.dp)
                .clickable(onClick = { onSelect(options[0]); isVisible = false }),
                horizontalArrangement = Arrangement.Center)
            {
                Text(options[0], fontFamily = bison, fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp)
            }

            if((options.size-1)>=1)
            {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable(onClick = { onSelect(options[1]); isVisible = false }),
                    horizontalArrangement = Arrangement.Center)
                {
                    Text(options[1], fontFamily = bison, fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp)
                }
            }

            if((options.size-1)>=2)
            {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable(onClick = { onSelect(options[2]); isVisible = false }),
                    horizontalArrangement = Arrangement.Center)
                {
                    Text(options[2], fontFamily = bison, fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp)
                }
            }

            if((options.size-1)>=3)
            {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable(onClick = { onSelect(options[3]); isVisible = false }),
                    horizontalArrangement = Arrangement.Center)
                {
                    Text(options[3], fontFamily = bison, fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp)
                }
            }
        }
    }
}

//  Upload information when done
fun fillProfileUpload(context: Context)
{
    appViewModel.saveEdits()
    appViewModel.uploadProfile(context)
}

@Composable
fun FieldComposable(value: String, onChange: (String)->Unit)
{
    val onSurface = MaterialTheme.colorScheme.onSurface
    Row(modifier = Modifier
        .fillMaxWidth(0.7f), verticalAlignment = Alignment.CenterVertically,
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

//  Grabs images from storage, enabling user to pick an image from gallery
@Composable
fun FillProfilePicComposable(galleryPicker: ManagedActivityResultLauncher<String, Uri?>, onClick: ()->Unit)
{
    val isUploading by remember { mutableStateOf(appViewModel.isUploading)}

    val transition = rememberInfiniteTransition()

    val rotationAnimation = transition.animateFloat(
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