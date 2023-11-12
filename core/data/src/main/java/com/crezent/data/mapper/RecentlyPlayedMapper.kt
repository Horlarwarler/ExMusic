package com.crezent.data.mapper

import com.crezent.database.model.RecentEntity
import com.crezent.models.RecentlyPlayed

fun RecentlyPlayed.toEntity(): RecentEntity {
    return RecentEntity(
        songId = songId,
        description = description,
        title = title,
        thumbnail = thumbnail

    )
}


