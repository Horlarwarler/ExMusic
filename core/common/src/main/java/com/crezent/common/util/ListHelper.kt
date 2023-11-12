package com.crezent.common.util

fun <T> List<T>.removeElement(index:Int = 0): List<T>{
    val toMutableList  =  toMutableList()
    toMutableList.removeAt(index)
    return  toMutableList.toList()
}

