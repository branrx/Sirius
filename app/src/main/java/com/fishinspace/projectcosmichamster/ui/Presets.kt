package com.fishinspace.projectcosmichamster.ui

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.fishinspace.projectcosmichamster.R


var defaultImage: ImageBitmap? = null
var defaultPath: String? = null

fun getDefaultImage(context : Context): ImageBitmap
{
    val img = BitmapFactory.decodeResource(context.resources, R.drawable.default_profile_ps)
    return img.asImageBitmap()
}

fun getAllDefaults(context : Context)
{
    defaultImage = getDefaultImage(context)
    defaultPath = context.filesDir.absolutePath
}