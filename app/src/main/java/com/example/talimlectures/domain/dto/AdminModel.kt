package com.example.talimlectures.domain.dto

@kotlinx.serialization.Serializable
data class AdminModel(
    val adminId:Int? = null,
    val username:String,
    val password:String,
    val isSuperAdmin:Boolean = false,
    val uniqueId : String? = null,
)
