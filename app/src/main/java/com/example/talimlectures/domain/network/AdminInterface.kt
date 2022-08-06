package com.example.talimlectures.domain.network

import com.example.talimlectures.domain.dto.AdminModel
import com.example.talimlectures.domain.dto.CategoryModel
import com.example.talimlectures.domain.dto.LectureModel
import com.example.talimlectures.domain.dto.Token
import com.example.talimlectures.util.Resource

interface AdminInterface {

    suspend fun  uploadLecture(
        lecture: LectureModel
    ):Resource<Unit>
    suspend fun  deleteLecture(
        uniqueId: String
    ):Resource<Unit>
    suspend fun updateLecture(
        lecture: LectureModel
    ):Resource<Unit>

    suspend fun updateCategory(
        categoryModel: CategoryModel
    ):Resource<Unit>

    suspend fun deleteCategory(
        uniqueId: String
    ):Resource<Unit>

    suspend fun createCategory(
        categoryModel: CategoryModel
    ):Resource<Unit>
    suspend fun login(
        adminModel: AdminModel
    ):Resource<Token>

    sealed class  EndPoints(val url : String){
        object uploadLecture: EndPoints(url = "upload-lecture")
        object deleteLecture: EndPoints(url = "delete-lecture")
        object login:EndPoints("login")
        object updateLecture: EndPoints("update-lecture")
        object updateCategory: EndPoints("update-category")
        object createCategory: EndPoints(url = "create-category")
        object deleteCategory: EndPoints(url = "delete-category")
    }
}