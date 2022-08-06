package com.example.talimlectures.domain.network

import com.example.talimlectures.domain.dto.AdminModel
import com.example.talimlectures.util.Resource

interface SuperAdminInterface {
    suspend fun  createUser(
        adminModel: AdminModel
    ):Resource<Unit>
    suspend fun deleteUser(
        username:String
    ):Resource<Unit>
    suspend fun getAllAdmins():Resource<List<AdminModel>>

    suspend fun  deleteAllAdmins():Resource<Unit>

    suspend fun  deleteAllLecture():Resource<Unit>

    suspend fun  deleteAllCategory():Resource<Unit>

    sealed class  EndPoints(val url : String){
        object createAdmin: EndPoints(url ="create-admin")
        object deleteAdmin: EndPoints(url ="delete-admin")
        object getAllAdmins: EndPoints(url ="admins")
        object deleteAllAdmins: EndPoints(url ="delete-all-admins")
        object deleteAllLecture: EndPoints(url ="delete-all-lectures")
        object deleteAllCategory:  EndPoints(url ="delete-all-categories")

    }


}