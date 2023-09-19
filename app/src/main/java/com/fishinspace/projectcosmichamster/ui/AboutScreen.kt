package com.fishinspace.projectcosmichamster.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.ui.theme.ProjectCosmicHamsterTheme

@Composable
fun AboutScreen()
{
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxSize())
            {

                Column(modifier = Modifier
                    .aspectRatio(1f)
                    .requiredSize(160.dp))
                {
                    Image(
                        painter = painterResource(id = R.drawable.cirqle_logo_3),
                        contentDescription = "coffee reminder logo",
                        modifier = Modifier,
                        contentScale = ContentScale.FillBounds
                    )
                }
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 52.sp,
                    modifier = Modifier,
                    fontFamily = bison,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )

                Text(
                    text = stringResource(id = R.string.app_version),
                    fontSize = 20.sp,
                    modifier = Modifier,
                    fontFamily = bison,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                )

                Text(
                    text = stringResource(id = R.string.organisation_name),
                    fontSize = 20.sp,
                    modifier = Modifier,
                    fontFamily = bison,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                )

                Text(
                    text = stringResource(id = R.string.year),
                    fontSize = 20.sp,
                    modifier = Modifier,
                    fontFamily = bison,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxSize()
                    .padding(bottom = 24.dp))
            {
                Text(
                    text = "Developed by",
                    fontSize = 18.sp,
                    modifier = Modifier,
                    fontFamily = bison,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Text(
                    text = stringResource(id = R.string.author_name),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 4.dp),
                    fontFamily = bison,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }


        }
    }
}