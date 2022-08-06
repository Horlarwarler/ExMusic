package com.example.talimlectures.domain.mapper

import com.example.talimlectures.data.model.Favourite
import com.example.talimlectures.data.model.DatabaseLectureModel

fun List<DatabaseLectureModel>.convertToFavourite(): List<Favourite>{
    val list = this
        .map {
            val favourite = Favourite(
                id = it.lectureId,
                lectureTitle = it.lectureTitle
            )
            favourite
        }
        .toList()
    return list
}