package com.fishinspace.projectcosmichamster.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController

@Composable
fun MenuComposable()
{
    val screenheight = LocalConfiguration.current.screenHeightDp
    ElevatedCard(modifier = Modifier.fillMaxWidth())
    {
        Column(modifier = Modifier.fillMaxWidth().padding(0.dp))
        {
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp).height((0.04f * screenheight).dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .clickable { navController.navigate(Destination.ProfileScreen.route) },
                verticalAlignment = Alignment.CenterVertically)
            {
                Icon(painter = painterResource(id = R.drawable.profile_circle_svgrepo_com),
                    contentDescription = "profile icon",
                    modifier = Modifier.clipToBounds().height((0.03f * screenheight).dp).weight(0.3f),
                    tint = MaterialTheme.colorScheme.secondary
                )

                Surface(modifier = Modifier
                    .weight(0.7f))
                {
                    Text(
                        text = "Profile",
                        fontFamily = bison,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth().padding(8.dp).height((0.04f * screenheight).dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .clickable { /**/ },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                Icon(painter = painterResource(id = R.drawable.settings_svgrepo_com),
                    contentDescription = "profile icon",
                    modifier = Modifier.clipToBounds().height((0.03f * screenheight).dp).weight(0.3f),
                    tint = MaterialTheme.colorScheme.secondary
                )

                Surface(modifier = Modifier
                    .weight(0.7f).alpha(0.2f))
                {
                    Text(
                        text = "Settings",
                        fontFamily = bison,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth().padding(8.dp).height((0.04f * screenheight).dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .clickable { navController.navigate(Destination.AboutScreen.route) },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                Icon(painter = painterResource(id = R.drawable.danger_circle_svgrepo_com),
                    contentDescription = "profile icon",
                    modifier = Modifier.clipToBounds().height((0.03f * screenheight).dp).weight(0.3f),
                    tint = MaterialTheme.colorScheme.secondary
                )

                Surface(modifier = Modifier
                    .weight(0.7f))
                {
                    Text(
                        text = "About",
                        fontFamily = bison,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth().padding(8.dp).height((0.04f * screenheight).dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .clickable { appViewModel.signOut() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                Icon(painter = painterResource(id = R.drawable.logout_svgrepo_com),
                    contentDescription = "profile icon",
                    modifier = Modifier.clipToBounds().height((0.03f * screenheight).dp).weight(0.3f),
                    tint = MaterialTheme.colorScheme.secondary
                )

                Surface(modifier = Modifier
                    .weight(0.7f))
                {
                    Text(
                        text = "Logout",
                        fontFamily = bison,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}