package com.crezent.models

import kotlinx.serialization.Serializable

@Serializable
data class FileResult(
    val title:String,
    val songId:String,
    val taskStatus: TaskStatus = TaskStatus.NONE,
    val progress:Int = 0,
    val error:String? = null
)

enum class  TaskStatus{
    NONE,
    ONGOING,
    DONE,
    ERROR,
    PAUSED
}