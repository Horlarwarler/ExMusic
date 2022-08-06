package com.example.talimlectures.data.remote

import android.content.SharedPreferences
import android.os.Build
import com.example.talimlectures.domain.dto.AdminModel
import com.example.talimlectures.domain.dto.CategoryModel
import com.example.talimlectures.domain.dto.LectureModel
import com.example.talimlectures.domain.dto.Token
import com.example.talimlectures.domain.network.AdminInterface
import com.example.talimlectures.domain.network.SuperAdminInterface
import com.example.talimlectures.util.Resource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.*

class AdminServiceImplementation(
    private val client: HttpClient,
    private val sharedPreference: SharedPreferences
): AdminInterface {
    override suspend fun uploadLecture(lecture: LectureModel): Resource<Unit> {
        return try{
            val response = client.post(AdminInterface.EndPoints.uploadLecture.url){
                contentType(ContentType.Application.Json)
                setBody(lecture)
            }
            if (response.status == HttpStatusCode.OK){
                Resource.Success(Unit)
            } else{
                Resource.Error("Unknown Error")
            }
        } catch (e:Exception){
            Resource.Error(e.toString())
        }
    }

    override suspend fun deleteLecture(uniqueId: String): Resource<Unit> {
       return try {
            val response = client.delete(AdminInterface.EndPoints.deleteLecture.url)
            if (response.status == HttpStatusCode.OK){
                Resource.Success(Unit)
            } else{
                Resource.Error("UnknownError")
            }
        } catch (e:Exception){
            Resource.Error(e.toString())
        }
    }

    override suspend fun updateLecture(lecture: LectureModel): Resource<Unit> {
        return try{
            val response = client.put(AdminInterface.EndPoints.updateLecture.url){
                contentType(ContentType.Application.Json)
                setBody(lecture)
            }
            if (response.status == HttpStatusCode.OK){
                Resource.Success(Unit)
            } else{
                Resource.Error("Unknown Error")
            }
        } catch (e:Exception){
            Resource.Error(e.toString())
        }
    }

    override suspend fun login(adminModel: AdminModel): Resource<Token> {
        try {
            val response = client.get(AdminInterface.EndPoints.login.url){
                contentType(ContentType.Application.Json)
                setBody(adminModel)
            }
            if (response.status == HttpStatusCode.OK){
                sharedPreference.edit()
                    .putString("jwtToken",response.body())
                    .apply()

            }
        }
    }

    override suspend fun updateCategory(categoryModel: CategoryModel): Resource<Unit> {
        return try{
            val response = client.put(AdminInterface.EndPoints.updateCategory.url){
                contentType(ContentType.Application.Json)
                setBody(categoryModel)
            }
            if (response.status == HttpStatusCode.OK){
                Resource.Success(Unit)
            } else{
                Resource.Error("Unknown Error")
            }
        } catch (e:Exception){
            Resource.Error(e.toString())
        }
    }

    override suspend fun deleteCategory(uniqueId: String): Resource<Unit> {
        return  try {
            val response = client.delete("${ AdminInterface.EndPoints.deleteCategory.url}/$uniqueId")
            if (response.status == HttpStatusCode.OK){
                Resource.Success(Unit)
            } else{
                Resource.Error("UnknownError")
            }
        } catch (e:Exception){
            Resource.Error(e.toString())
        }
    }

    override suspend fun createCategory(categoryModel: CategoryModel): Resource<Unit> {
        return try{
            val response = client.post(AdminInterface.EndPoints.createCategory.url){
                contentType(ContentType.Application.Json)
                setBody(categoryModel)
            }
            if (response.status == HttpStatusCode.OK){
                Resource.Success(Unit)
            } else{
                Resource.Error("Unknown Error")
            }
        } catch (e:Exception){
            Resource.Error(e.toString())
        }
    }
}