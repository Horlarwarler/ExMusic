package com.crezent.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crezent.models.RecentlyPlayed


@Entity(tableName = "RECENTLY_PLAYED")
data class RecentEntity(
    @PrimaryKey()
    val songId:String,
    val title:String,
    val description:String,
    val thumbnail: String?

)

fun RecentEntity.mapToModel(): RecentlyPlayed {
    return RecentlyPlayed(
        songId = songId,
        description = description,
        title = title,
        thumbnail = thumbnail

    )
}