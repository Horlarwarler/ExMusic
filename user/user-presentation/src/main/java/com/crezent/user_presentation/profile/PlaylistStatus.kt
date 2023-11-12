package com.crezent.user_presentation.profile

import com.crezent.models.TaskStatus


data class PlaylistStatus(
    val songId:String,
    val taskStatus: com.crezent.models.TaskStatus = com.crezent.models.TaskStatus.NONE,
    val progress:Int = 0
)
