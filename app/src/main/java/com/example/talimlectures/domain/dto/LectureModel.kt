package com.example.talimlectures.domain.dto

@kotlinx.serialization.Serializable
data class LectureModel(
    val uniqueId:String? = null,
    val date: String? = null,
    val categoryId: Int? = null,
    val lectureDescription: String,
    val lectureId: Int,
    val lectureTitle: String,
    val length: Double

)

