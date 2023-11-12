package com.crezent.common.util

fun Long.currentTime(): String {
    val hour = (this/3600000)% 24
    val min = (this/60000)% 60
    val sec = (this/1000)%60
    return ("${if(hour > 0){"$hour:"} else {""} } ${if (min<10){"0$min"} else min} :${if (sec<10){"0$sec"} else sec}")
}
fun Long.totalTime(): String {
    val hour = (this/3600000)% 24
    val min = (this/60000)% 60
    val sec = (this/1000)%60
    return " ${if(hour > 0){"$hour:"} else {""} }   $min : $sec"

}