package com.fishinspace.projectcosmichamster.ui

import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date


fun getTime(timeLong: String): String
{
    //  Compare list
    var compareList = listOf(Calendar.DAY_OF_MONTH, Calendar.MONTH, Calendar.YEAR)
    var daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    var dateObj = Date(timeLong.toLong())
    var calendarObj = Calendar.getInstance()
    calendarObj.time = dateObj

    var temp = calendarObj.get(Calendar.HOUR_OF_DAY)
    val hour = if(temp<10){"0${temp}"}else{temp.toString()}

    temp = calendarObj.get(Calendar.MINUTE)
    val minute = if(temp<10){"0${temp}"}else{temp.toString()}

    var day = calendarObj.get(Calendar.DAY_OF_WEEK)

    //  Create current day calendar object for comparison
    var currentObj = Calendar.getInstance()
    currentObj.time = Date(Date().time)

    compareList.forEach { if(calendarObj.get(it)!=currentObj.get(it))
    {return "${daysOfWeek[calendarObj.get(Calendar.DAY_OF_WEEK)]} $hour:$minute"} }

    return "$hour:$minute"
}

fun getPostTime(timeLong: String): String
{
    val currentTime = Date().time
    val postTimeTemp = currentTime - timeLong.toLong()
    val postTime = if((postTimeTemp/60)/60>0){"${(postTimeTemp/60)/60}hrs"}else{(postTimeTemp/60)/60>0}
    return "${postTime}hrs"
}