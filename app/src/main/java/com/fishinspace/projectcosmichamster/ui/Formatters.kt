package com.fishinspace.projectcosmichamster.ui

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date

fun millisToDate()
{
    var simpleTime = SimpleDateFormat.getTimeInstance()
    var formattedDate = simpleTime.format(Date(1693899890306))
    Log.d("formatted time", formattedDate)
}