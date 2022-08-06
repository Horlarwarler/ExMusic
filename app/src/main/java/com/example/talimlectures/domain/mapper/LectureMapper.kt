package com.example.talimlectures.domain.mapper

import com.example.talimlectures.data.model.DatabaseLectureModel
import com.example.talimlectures.domain.dto.LectureModel

fun LectureModel.convertToLecture():DatabaseLectureModel{
    return DatabaseLectureModel(
        lectureId = this.lectureId,
        lectureTitle = this.lectureTitle,
        categoryId = this.categoryId,
        length = this.length,
        date = this.date!!,
        lectureDescription = this.lectureDescription,
        uniqueId = this.uniqueId!!
    )
}

fun DatabaseLectureModel.convertToLectureModel():LectureModel{
    return LectureModel(
        lectureId = this.lectureId,
        lectureTitle = this.lectureTitle,
        categoryId = this.categoryId,
        length = this.length,
        date = this.date,
        lectureDescription = this.lectureDescription,
        uniqueId = this.uniqueId
    )
}
