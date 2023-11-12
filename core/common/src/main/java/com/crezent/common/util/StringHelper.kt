package com.crezent.common.util

fun String?.nullIfTheSame(newValue:String?):String? {
    if (newValue == this){
        return null
    }
    return newValue
}