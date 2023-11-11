package com.fishinspace.projectcosmichamster.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fishinspace.projectcosmichamster.Destination
import com.fishinspace.projectcosmichamster.R
import com.fishinspace.projectcosmichamster.appViewModel
import com.fishinspace.projectcosmichamster.navController

@Composable
fun LoadingOverlay()
{
    val transition = rememberInfiniteTransition(label = "")

    val rotationAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing)), label = "loading"
    )

    Surface(shape = RoundedCornerShape(50),
        modifier = Modifier
            .aspectRatio(1f)
            .requiredSize(40.dp)
            .rotate(rotationAnimation.value),
        border = BorderStroke(4.dp,
            brush = Brush.sweepGradient(listOf(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.inversePrimary))),
        color = Color.Transparent
    )
    {
        /*Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Icon(painter = painterResource(id = R.drawable.compass_svgrepo_com),
                contentDescription = "loading icon",
                modifier = Modifier)
        }*/
    }
}