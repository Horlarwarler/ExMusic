package com.example.talimlectures.domain.dto

@kotlinx.serialization.Serializable
data class CategoryModel (
    val uniqueId: String? =  null,
    val categoryId :Int? = null,
    val categoryName:String,

    )
