package com.example.talimlectures.data.local.repository

import com.example.talimlectures.data.model.*
import com.example.talimlectures.util.Resource
import kotlinx.coroutines.flow.Flow

interface LectureRepoInterface {

    //lectures
    suspend fun getAllLecture(searchText:String): Flow<Resource<List<DatabaseLectureModel>>>
    suspend fun updateLecture(lecture: DatabaseLectureModel)
    suspend fun deleteLecture(lecture: DatabaseLectureModel)
    suspend fun getSelectedLecture(lectureName: String):Flow<Resource<DatabaseLectureModel>>

    // Category
    suspend fun getAllCategory():Flow<Resource<List<DatabaseCategoryModel>>>
    suspend fun updateCategory(databaseCategoryModel: DatabaseCategoryModel)
    suspend fun deleteCategory(databaseCategoryModel: DatabaseCategoryModel)

    //Favourite

    suspend fun getFavourites():Flow<Resource<List<Favourite>>>
    suspend fun addFavourite(favourite: Favourite)
    suspend fun  deleteFavourite(lectureTitle: String)

    //Recently Played
    suspend fun getRecentPlayed():Flow<Resource<List<RecentlyPlayed>>>
    suspend fun addRecentlyPlayed(recentlyPlayed: RecentlyPlayed)
    suspend fun deleteRecent(recentlyPlayed: RecentlyPlayed)

    // Newly Added
    suspend fun getNewLecture():Flow<Resource<List<NewlyAdded>>>
    suspend fun addNewLecture(newlyAdded: NewlyAdded)
    suspend fun deleteNewLecture(newlyAdded: NewlyAdded)

    // Work  Request
    suspend fun  getAllRequest(): List<WorkRequest>
    suspend fun  getRequest(lectureName: String): WorkRequest
    suspend fun insertRequest(newRequest: WorkRequest)
    suspend fun deleteRequest(request: WorkRequest)
    suspend fun  deleteAllRequest()

}