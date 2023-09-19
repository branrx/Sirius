package com.fishinspace.projectcosmichamster.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpScreen()
{
    //  Store input captured for email and password
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    Column(modifier = Modifier
        .fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(bottom = 72.dp)
        )
        {
            Image(
                painter = painterResource(id = R.drawable.cirqle_logo_3),
                contentDescription = "app icon", modifier = Modifier.requiredSize(100.dp)
                    .clipToBounds(),
            )
            Text(text = stringResource(id = R.string.app_name), modifier = Modifier
                .padding(top=12.dp), fontFamily = bison,
                fontWeight = FontWeight.Bold, letterSpacing = 1.sp, fontSize = 40.sp)
            Text(text = "Sign-Up", modifier = Modifier
                , fontFamily = bison,
                fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 24.sp)
        }

        //  Captures input for email
        EmailComposable(email, onChange = {email = it})

        Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.fillMaxWidth(0.7f))

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth(0.7f))
        {
            Text(text = "EMAIL", modifier = Modifier, fontFamily = bison,
                fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
        }

        //  Captures input for password
        PasswordComposable(password, onChange = {password = it})

        Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.fillMaxWidth(0.7f))

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth(0.7f))
        {
            Text(text = "PASSWORD", modifier = Modifier, fontFamily = bison,
                fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
        }

        ElevatedButton(onClick = { appViewModel.signUp(email = email, password = password)
            navController.navigate(Destination.FillProfileScreen.route)
                         }, modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(top = 64.dp), shape = RoundedCornerShape(12.dp))
        {
            Text("Sign Up", fontFamily = bison,
                fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
        }
    }

}