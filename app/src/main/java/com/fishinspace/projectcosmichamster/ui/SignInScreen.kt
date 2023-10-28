package com.fishinspace.projectcosmichamster.ui

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.activityContext
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignInScreen()
{
    //  Helps show loading animation when logging in
    var isLoading by remember { mutableStateOf(appViewModel.isLoading) }

    val relocation = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()

    //  Reinitiates the view model when user logs out
    var init by remember { mutableStateOf(false)}
    if(!init)
    {
        appViewModel = viewModel()
        init = true
        Log.d("viewmodel", "viewModel re-initialised")
    }

    //  Stores variables for email and password when logging in
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    val screenheight = LocalConfiguration.current.screenHeightDp

    Box()
    {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState(), enabled = true)
            .fillMaxSize(), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(16.dp)
                .height((0.3f * screenheight).dp)
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            )
            {
                //  Displays the application's main logo
                Icon(
                    painter = painterResource(id = R.drawable.cirqle_logo_3),
                    contentDescription = "app icon", modifier = Modifier
                        .weight(0.5f)
                        .padding(24.dp)
                        .clipToBounds(),
                )
                Text(text = stringResource(id = R.string.app_name), modifier = Modifier
                    .padding(top = 12.dp), fontFamily = bison,
                    fontWeight = FontWeight.Bold, letterSpacing = 1.sp, fontSize = 40.sp)
                Text(text = "Sign-In", modifier = Modifier, fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 24.sp)
            }

            Column(modifier = Modifier.height((0.5f * screenheight).dp), verticalArrangement = Arrangement.Center)
            {
                //  Has the input composable that captures email input
                EmailComposable(email, onChange = {email = it})

                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 20.dp, top = 4.dp))
                {
                    Text(text = "EMAIL", modifier = Modifier.alpha(0.6f), fontFamily = bison,
                        fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
                }

                //  Has the input composable that captures password input
                PasswordComposable(password, onChange = {password = it},
                    onFocus = {if(it.isFocused) scope.launch { delay(300); relocation.bringIntoView() }},
                    passwordType = PasswordType.NORMAL,
                    onDone = {if(validateCreds(email, password)){
                            appViewModel.signIn(email = email, password = password)
                        }else{
                            Toast.makeText(activityContext, "Email or password cannot be empty!", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 20.dp, top = 4.dp))
                {
                    Text(text = "PASSWORD", modifier = Modifier.alpha(0.6f), fontFamily = bison,
                        fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
                }

                ElevatedButton(onClick = {
                    if(validateCreds(email, password)){
                        appViewModel.signIn(email = email, password = password)
                    }else{
                        Toast.makeText(activityContext, "Email or password cannot be empty!", Toast.LENGTH_SHORT).show()
                    } },
                    modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(top = 32.dp), shape = RoundedCornerShape(12.dp))
                {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center)
                    {
                        Column(modifier = Modifier.weight(0.9f), horizontalAlignment = Alignment.CenterHorizontally)
                        {
                            Text("Log In", fontFamily = bison,
                                fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
                        }
                    }
                }
            }

            Column(modifier = Modifier.height((0.2f * screenheight).dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                //  Takes user to signup if they don't have an account
                Surface(modifier = Modifier
                    .clip(shape = RoundedCornerShape(25))
                    .clickable { navController.navigate(Destination.SignUpScreen.route) }
                    .padding(top = 0.dp)
                )
                {
                    Row(horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top=8.dp, bottom=8.dp)
                    )
                    {
                        Text(text = "Don't have an account?", modifier = Modifier.alpha(0.6f)
                            , fontFamily = bison,
                            fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 20.sp)
                    }
                }

                //  Takes user to reset their password
                Surface(modifier = Modifier
                    .clip(shape = RoundedCornerShape(25))
                    .clickable { navController.navigate(Destination.PasswordResetScreen.route) }
                    .padding(top = 0.dp),
                )
                {
                    Row(horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top=8.dp, bottom=8.dp)
                    )
                    {
                        Text(text = "Forgot password?", modifier = Modifier.alpha(0.6f),
                            fontFamily = bison, fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

//  Captures email input
@Composable
fun EmailComposable(value: String, onChange: (String)->Unit)
{
    val onSurface = MaterialTheme.colorScheme.onSurface

    Surface(color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(0.7f),
        shape = RoundedCornerShape(12.dp)
    )
    {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column(modifier = Modifier
                .weight(0.15f)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally)
            {
                Icon(painter = painterResource(id = R.drawable.letter_unread_svgrepo_com), contentDescription = "email icon")
            }
            Column(modifier = Modifier.weight(0.7f))
            {
                BasicTextField(value = value, onValueChange = onChange, modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Start,
                    fontFamily = yanone, letterSpacing = 1.sp, fontSize = 18.sp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    cursorBrush = Brush.sweepGradient(listOf(onSurface, onSurface)),
                    singleLine = true,
                )
            }
        }
    }
}

//  Captures email input
@Composable
fun PasswordComposable(value: String, onChange: (String)->Unit, onFocus: (FocusState)->Unit, passwordType: PasswordType,
                       onDone: (KeyboardActionScope.()->Unit)
)
{
    val onSurface = MaterialTheme.colorScheme.onSurface
    var showPassword by remember { mutableStateOf(false) }

    Surface(color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(0.7f),
        shape = RoundedCornerShape(12.dp)
    )
    {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        )
        {

            Column(modifier = Modifier
                .weight(0.15f)
                .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Icon(painter = painterResource(id = R.drawable.lock_password_svgrepo_com), contentDescription = "email icon")
            }
            Column(modifier = Modifier.weight(0.7f))
            {
                BasicTextField(value = value, onValueChange = onChange, modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent(onFocusEvent = onFocus),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Start,
                        fontFamily = yanone, letterSpacing = 1.sp, fontSize = 18.sp),
                    cursorBrush = Brush.sweepGradient(listOf(onSurface, onSurface)),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = onDone),
                    visualTransformation = if(showPassword)
                    { VisualTransformation.None}else{PasswordVisualTransformation()}
                )
            }
            Column(modifier = Modifier
                .weight(0.15f)
                .clickable { showPassword = !showPassword }
                .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Icon(painter = painterResource(id = if(showPassword){R.drawable.eye_slash_svgrepo_com__1_}else{R.drawable.eye_svgrepo_com__2_}),
                    contentDescription = "password icon")
            }
        }
    }
}

//  Checks if credentials are empty
fun validateCreds(email: String, password: String): Boolean
{
    return email.isNotEmpty() && password.isNotEmpty()
}