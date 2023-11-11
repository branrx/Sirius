package com.fishinspace.projectcosmichamster.ui

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
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
    var context = LocalContext.current

    val screenheight = LocalConfiguration.current.screenHeightDp

    val relocation = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        , verticalArrangement = Arrangement.Center,
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
            Icon(
                painter = painterResource(id = R.drawable.cirqle_logo_3),
                contentDescription = "app icon", modifier = Modifier
                    .padding(24.dp)
                    .weight(0.5f)
                    .clipToBounds(),
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(text = stringResource(id = R.string.app_name), modifier = Modifier
                .padding(top=12.dp), fontFamily = bison,
                fontWeight = FontWeight.Bold, letterSpacing = 1.sp, fontSize = 40.sp,
                color = MaterialTheme.colorScheme.secondary)
            Text(text = "Sign-Up", modifier = Modifier
                , fontFamily = bison,
                fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 24.sp)
        }

        Column(modifier = Modifier.height((0.5f * screenheight).dp), verticalArrangement = Arrangement.Bottom)
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
            PasswordComposable(password, onChange = {password = it}, onFocus = {if(it.isFocused) scope.launch { delay(300); relocation.bringIntoView() }},
                passwordType = PasswordType.NORMAL, onDone = { /**/ })

            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 8.dp, top = 4.dp))
            {
                Text(text = "PASSWORD", modifier = Modifier, fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }

            // Re-enter password
            PasswordComposable(password2, onChange = {password2 = it}, onFocus = {if(it.isFocused) scope.launch { delay(300); relocation.bringIntoView() }},
                passwordType = PasswordType.RE_ENTRY, onDone = { signUp(password, password2, context = context) })

            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(bottom = 20.dp, top = 4.dp))
            {
                Text(text = "REENTER-PASSWORD", modifier = Modifier, fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }

            ElevatedButton(onClick = {
                signUp(password, password2, context = context)
            }, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(top = 32.dp), shape = RoundedCornerShape(12.dp))
            {
                Text("Sign Up", fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }
        }
        Spacer(modifier = Modifier.height((0.2f * screenheight).dp))
    }
}

//  Sign up handler
fun signUp(password: String, password2:String, context: Context)
{
    if(validatePassword(password, password2))
    {
        /*appViewModel.signUp(email = email, password = password, context = context)*/
        navController.navigate(Destination.FillProfileScreen.route)
    }   else
    {
        Toast.makeText(context,"Passwords don't match.", Toast.LENGTH_SHORT).show()
    }
}

fun validatePassword(password1: String, password2: String): Boolean
{
    return password1 == password2
}