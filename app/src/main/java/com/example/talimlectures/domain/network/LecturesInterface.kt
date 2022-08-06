package com.example.talimlectures.domain.network

import com.example.talimlectures.domain.dto.CategoryModel
import com.example.talimlectures.domain.dto.LectureModel
import com.example.talimlectures.util.Resource
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LecturesInterface {
         suspend fun getAllLecture():Resource<List<LectureModel>>
        suspend fun getAllCategories(): Resource<List<CategoryModel>>

         @GET("/download")
         suspend fun getLecture(
             @Query("lectureName")lectureName:String
         ):Response<ResponseBody>


}