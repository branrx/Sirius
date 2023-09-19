package com.fishinspace.projectcosmichamster.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignInScreen()
{
    //  Helps show loading animation when logging in
    var isLoading by remember { mutableStateOf(appViewModel.isLoading) }

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

    Box()
    {
        Column(modifier = Modifier
            .fillMaxSize(), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Spacer(modifier = Modifier.weight(0.1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 72.dp)
            )
            {
                //  Displays the application's main logo
                Image(
                    painter = painterResource(id = R.drawable.cirqle_logo_3),
                    contentDescription = "app icon", modifier = Modifier
                        .requiredSize(100.dp)
                        .clipToBounds(),
                )
                Text(text = stringResource(id = R.string.app_name), modifier = Modifier
                    .padding(top = 12.dp), fontFamily = bison,
                    fontWeight = FontWeight.Bold, letterSpacing = 1.sp, fontSize = 40.sp)
                Text(text = "Sign-In", modifier = Modifier, fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 24.sp)
            }

            //  Has the input composable that captures email input
            EmailComposable(email, onChange = {email = it})

            Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(top = 4.dp))

            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth(0.7f))
            {
                Text(text = "EMAIL", modifier = Modifier, fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }

            //  Has the input composable that captures password input
            PasswordComposable(password, onChange = {password = it})

            Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(top = 4.dp))

            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth(0.7f))
            {
                Text(text = "PASSWORD", modifier = Modifier, fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }

            ElevatedButton(onClick = {
                appViewModel.signIn(email = email, password = password) }, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(top = 64.dp), shape = RoundedCornerShape(12.dp))
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

            Spacer(modifier = Modifier.weight(0.1f))

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
                    Text(text = "Don't have an account?", modifier = Modifier
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
                    Text(text = "Forgot password?", modifier = Modifier, fontFamily = bison,
                        fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}

//  Captures email input
@Composable
fun EmailComposable(value: String, onChange: (String)->Unit)
{
    val onSurface = MaterialTheme.colorScheme.onSurface
    Row(modifier = Modifier
        .fillMaxWidth(0.7f), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center)
    {
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(0.8f),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            BasicTextField(value = value, onValueChange = onChange, modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Start),
                cursorBrush = Brush.sweepGradient(listOf(onSurface, onSurface)),
                singleLine = true
            )
        }
    }
}

//  Captures email input
@Composable
fun PasswordComposable(value: String, onChange: (String)->Unit)
{
    val onSurface = MaterialTheme.colorScheme.onSurface
    Row(modifier = Modifier
        .padding(top = 32.dp)
        .fillMaxWidth(0.7f), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center)
    {
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(0.8f),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            BasicTextField(value = value, onValueChange = onChange, modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Start),
                cursorBrush = Brush.sweepGradient(listOf(onSurface, onSurface)),
                singleLine = true
            )
        }
    }
}