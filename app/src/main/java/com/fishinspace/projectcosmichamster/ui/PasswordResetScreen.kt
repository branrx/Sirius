package com.fishinspace.projectcosmichamster.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.activityContext
import com.fishinspace.projectcosmichamster.appViewModel

@Composable
fun PasswordResetScreen()
{
    //  Field value: e.g field: age ,field value = 24
    var value by remember { mutableStateOf("") }

    //  Field index: translates to username, name, surname, age fields etc..
    var fieldIndex by remember { mutableIntStateOf(0) }

    //  If reset email sent, shows guides to follow
    var emailSent by remember { mutableStateOf(false)}

    //  Guides users as to what to do.
    var guide: String = when(emailSent)
    {
        false -> "Email associated with your account."
        else -> "Please check your email for the reset link."
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
            )

            Text(text = stringResource(id = R.string.app_name), modifier = Modifier
                .padding(top=12.dp), fontFamily = bison,
                fontWeight = FontWeight.Bold, letterSpacing = 1.sp, fontSize = 40.sp)
            Text(text = "Reset Password", modifier = Modifier
                , fontFamily = bison,
                fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 24.sp)
        }

        Column(modifier = Modifier.weight(0.5f), verticalArrangement = Arrangement.Center)
        {
            AnimatedVisibility(visible = !emailSent)
            {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(bottom = 16.dp))
                {
                    Text(text = "enter your email.", modifier = Modifier, fontFamily = bison,
                        fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
                }
            }

            //  Captures the field value, be it name, age, school etc...
            AnimatedVisibility(visible = !emailSent)
            {
                ResetEmailInputComposable(value = value, onChange = { value = it})
            }

            //  Displays name of the field
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(top = 16.dp))
            {
                Text(text = guide,
                    modifier = Modifier.alpha(0.5f), fontFamily = yanone,
                    fontWeight = FontWeight.Normal, letterSpacing = 1.sp, fontSize = 18.sp,
                    textAlign = TextAlign.Center)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(0.7f)
            .weight(0.2f),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center)
        {
            ElevatedButton(onClick = {
                //  Move to next field only if value is not empty
                if(value.isNotEmpty())
                {
                    appViewModel.resetPassword(value)
                    emailSent = true
                }else{
                    Toast.makeText(activityContext ,"No email entered.", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(start = 2.dp)
                .padding(bottom = 32.dp),
                enabled = !emailSent
            )
            {
                Text("Reset Password", fontFamily = bison,
                    fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp, fontSize = 18.sp)
            }
        }

    }

}

@Composable
fun ResetEmailInputComposable(value: String, onChange: (String)->Unit)
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
            Column(modifier = Modifier
                .weight(0.7f)
                .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                BasicTextField(value = value, onValueChange = onChange, modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Start),
                    cursorBrush = Brush.sweepGradient(listOf(onSurface, onSurface)),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {resetPassword(value)})
                )
            }
        }
    }
}

fun resetPassword(email: String)
{
    if(email.isNotEmpty())
    {
        appViewModel.resetPassword(email)
    }else{
        Toast.makeText(activityContext ,"No email entered.", Toast.LENGTH_SHORT).show()
    }
}

//  email validator
//  val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
//  email.matches(emailRegex) // returns boolean