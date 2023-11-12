package com.crezent.creator_domain.models


data class Admin(
    val adminId:Int? = null,
    val username:String,
    val password:String,
    val isSuperAdmin:Boolean? = null,
    val uniqueId : String?,
)
