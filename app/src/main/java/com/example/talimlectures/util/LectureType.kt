package com.example.talimlectures.util

import com.example.talimlectures.data.model.Favourite
import com.example.talimlectures.data.model.DatabaseLectureModel
import com.example.talimlectures.data.model.NewlyAdded
import com.example.talimlectures.data.model.RecentlyPlayed

sealed class LectureType <T>(val data :T ) {
    class RecentlyPlayedType (data : RecentlyPlayed): LectureType<RecentlyPlayed>(data= data)
    class FavouriteLectureType (data : Favourite): LectureType<Favourite>(data= data)
    class NewlyAddedType (data : NewlyAdded): LectureType<NewlyAdded>(data= data)
}

fun <T> DatabaseLectureModel.convertLectureToLectureType(lectureType: LectureType<T>):DatabaseLectureModel{
    return DatabaseLectureModel(
        categoryId = this.categoryId,
        date = this.date,
        lectureTitle = this.lectureTitle,
        lectureDescription = this.lectureDescription,
        length = this.length,
        lectureId = when(
            lectureType
        ){
            is LectureType.RecentlyPlayedType -> {
                lectureType.data.id
            }
            is LectureType.FavouriteLectureType -> {
                lectureType.data.id
            }
            is LectureType.NewlyAddedType -> {
                lectureType.data.id
            }
        },
        uniqueId = this.uniqueId
    )
}

