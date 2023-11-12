package com.crezent.common.util

import android.content.Context

sealed interface UiText {
    data class  DynamicText(val message: String) : UiText
    data class ResourceText(val resId:Int): UiText

    fun asString(context:Context):String {
        return when(this){
            is DynamicText -> {
                    message
            }
            is ResourceText -> {
                    context.getString(resId)
            }
        }
    }
}