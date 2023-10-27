package com.fishinspace.projectcosmichamster.ui

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun SignUpScreen()
{
    //  Store input captured for email and password
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var password2 by remember { mutableStateOf("")}

    val screenheight = LocalConfiguration.current.screenHeightDp

    val relocation = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        , verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        LazyColumn(modifier = Modifier
            .weight(1f)
            .background(Color.Blue))
        {
            item{
                Column(modifier = Modifier
                    .height((1f / 2 * screenheight).dp)
                    .background(Color.Green),
                    verticalArrangement = Arrangement.Center)
                {
                    PasswordComposable(password, onChange = {password = it}, onFocus = {if(it.isFocused)
                        scope.launch { delay(300); relocation.bringIntoView() }},
                    passwordType = PasswordType.NORMAL,
                    onDone = {})
                }

                PasswordComposable(password, onChange = {password = it}, onFocus = {if(it.isFocused)
                    scope.launch { delay(300); relocation.bringIntoView() }},
                    passwordType = PasswordType.NORMAL,
                    onDone = {})
                Column(modifier = Modifier
                    .height((1f / 2 * screenheight).dp)
                    .background(Color.Red),
                    verticalArrangement = Arrangement.Center)
                {
                    PasswordComposable(password, onChange = {password = it}, onFocus = {if(it.isFocused)
                    scope.launch { delay(300); relocation.bringIntoView() }},
                        passwordType = PasswordType.RE_ENTRY,
                        onDone = {})
                    Spacer(modifier = Modifier.height(16.dp))
                    ElevatedButton(onClick = {
                        if(validatePassword(password, password2))
                        {
                            /*appViewModel.signUp(email = email, password = password)*/
                            navController.navigate(Destination.FillProfileScreen.route)
                        }   else
                        {
                            Toast.makeText(activityContext ,"Passwords don't match.", Toast.LENGTH_SHORT).show()
                        }
                    }, modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .focusable(enabled = true)
                        .bringIntoViewRequester(relocation)
                        .padding(top = 32.dp), shape = RoundedCornerShape(12.dp))
                    {
                        Text("Sign Up", fontFamily = bison,
                            fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
                    }

                }
            }
        }
        /*Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
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
                    .padding(24.dp)
                    .weight(0.5f)
                    .clipToBounds(),
            )
            Text(text = stringResource(id = R.string.app_name), modifier = Modifier
                .padding(top=12.dp), fontFamily = bison,
                fontWeight = FontWeight.Bold, letterSpacing = 1.sp, fontSize = 40.sp)
            Text(text = "Sign-Up", modifier = Modifier
                , fontFamily = bison,
                fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 24.sp)
        }

        Column(modifier = Modifier.weight(0.5f), verticalArrangement = Arrangement.Bottom)
        {
            //  Captures input for email
            EmailComposable(email, onChange = {email = it})

            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 20.dp, top = 4.dp))
            {
                Text(text = "EMAIL", modifier = Modifier, fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }

            //  Captures input for password
            PasswordComposable(password, onChange = {password = it})

            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 8.dp, top = 4.dp))
            {
                Text(text = "PASSWORD", modifier = Modifier, fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }

            // Re-enter password
            PasswordComposable(password2, onChange = {password2 = it})

            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 20.dp, top = 4.dp))
            {
                Text(text = "REENTER-PASSWORD", modifier = Modifier, fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }

            ElevatedButton(onClick = {
                if(validatePassword(password, password2))
                {
                    /*appViewModel.signUp(email = email, password = password)*/
                    navController.navigate(Destination.FillProfileScreen.route)
                }   else
                {
                    Toast.makeText(activityContext ,"Passwords don't match.", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(top = 32.dp), shape = RoundedCornerShape(12.dp))
            {
                Text("Sign Up", fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.weight(0.2f))*/
    }

}

fun validatePassword(password1: String, password2: String): Boolean
{
    return password1 == password2
}