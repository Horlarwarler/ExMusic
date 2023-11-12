package com.crezent.ExMusic.util

fun String?.nullIfTheSame(newValue:String?):String?{
    val valueTheSame = this == newValue
    return if (valueTheSame) null else newValue
}