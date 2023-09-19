package com.fishinspace.projectcosmichamster.ui

import android.content.Intent
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.fishinspace.projectcosmichamster.R

//  Fonts
val bison = FontFamily(
    Font(R.font.bison, weight = FontWeight.Normal),
    Font(R.font.bison_demibold, weight = FontWeight.SemiBold),
    Font(R.font.bison_bold, weight = FontWeight.Bold)
)

val ebrima = FontFamily(
    Font(R.font.ebrima, weight = FontWeight.Normal)
)

val yanone = FontFamily(
    Font(R.font.yanonekaffeesatz, weight = FontWeight.Normal)
)

//  Stores service intent build
var serviceIntent: Intent? = null