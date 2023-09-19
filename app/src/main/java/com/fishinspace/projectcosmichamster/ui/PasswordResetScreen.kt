package com.fishinspace.projectcosmichamster.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.activityContext
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController

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
            .padding(top = 72.dp),
            verticalArrangement = Arrangement.Center
        )
        {
            Image(
                painter = painterResource(id = R.drawable.cirqle_logo_3),
                contentDescription = "app icon", modifier = Modifier
                    .requiredSize(100.dp)
                    .clipToBounds(),
            )

            Text(text = stringResource(id = R.string.app_name), modifier = Modifier
                .padding(top=12.dp), fontFamily = bison,
                fontWeight = FontWeight.Bold, letterSpacing = 1.sp, fontSize = 40.sp)
        }

        Spacer(modifier = Modifier.weight(0.1f))

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

        AnimatedVisibility(visible = !emailSent)
        {
            Divider(thickness = Dp.Hairline, color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth(0.7f).padding(top = 4.dp))
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

        Spacer(modifier = Modifier.weight(0.1f))

        Row(modifier = Modifier.fillMaxWidth(0.7f))
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

            ElevatedButton(onClick = {
                //  Move to next field only if value is not empty
                if(value.isNotEmpty())
                {
                    appViewModel.resetPassword(value)
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
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center),
                cursorBrush = Brush.sweepGradient(listOf(onSurface, onSurface)),
                singleLine = true
            )
        }
    }
}