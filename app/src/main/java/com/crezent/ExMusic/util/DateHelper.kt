package com.crezent.ExMusic.util

import android.util.Log
import java.util.Calendar
import java.util.UUID


fun String.extractTimeFromString():Triple<Int?, Int, Int>{
   val splitDate = this.split(":")
    //02:01:14
    val containsHr = splitDate.size == 3
    val hr  = splitDate[0].toIntOrNull()
    val min = if (containsHr) splitDate[1].toInt() else  splitDate[0].toInt()
    val sec =  if (containsHr) splitDate[2].toInt() else  splitDate[1].toInt()

    return when(splitDate.size) {
        3 -> Triple(hr, min, sec)
        2 -> Triple(null, min, sec)
        else -> throw  IllegalArgumentException("Date Not Valid")
    }
}

fun Triple<Int?, Int, Int>.extractStringFromTime():String{
    val hr  = first.addZeroString()
    val min = second.addZeroString()
    val sec =  third.addZeroString()

    return when(first) {
        null -> "$min:$sec"
        else -> "$hr:$min:$sec"
    }
}

fun Triple<Int?, Int, Int>.increaseTime():Triple<Int?, Int, Int>{
    var hr =  first
    var min = second
    var sec = third
    sec += 1
    Log.d("LOG", " Increase $this")

    when {

        third > 59 -> {
            min += 1
            sec = 0
        }
        second > 59 -> {
            hr?.let {
                hr = hr!! + 1
            }
            hr?:run {
                hr = 1
            }
            min = 0

        }
        hr!= null  -> {
            if (hr!! > 2){
                throw  Exception("Time Elapsed")

            }
        }


    }

    return  Triple(hr, min,sec)
}

private fun Int?.addZeroString():String? {
    return when (this) {
        null ->{
            null
        }
        in 0..9 ->{
            "0$this"
        }
        else -> {
            this.toString()
        }
    }
}

fun generateTodayDate(): String {
    val calendar = Calendar.getInstance()
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    return "$day:$month"
}

fun generateRandomString():String {
    return UUID.randomUUID().toString()
}