package com.example.talimlectures.data.local.repository

import com.example.talimlectures.data.local.database.*
import com.example.talimlectures.data.model.*
import com.example.talimlectures.domain.mapper.convertToCategory
import com.example.talimlectures.domain.mapper.convertToLecture
import com.example.talimlectures.domain.network.LecturesInterface
import com.example.talimlectures.util.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

@ViewModelScoped
class LectureRepository @Inject constructor(
    private val lectureDao: LectureDao,
    private val recentPlayDao: RecentPlayDao,
    private val categoryDao: CategoryDao,
    private val favouriteDao: FavouriteDao,
    private val newLecturesDao: NewLecturesDao,
    private val lecturesInterface: LecturesInterface,
    private val workRequest: WorkRequestDao
) : LectureRepoInterface{
    override suspend fun getAllLecture(searchText: String): Flow<Resource<List<DatabaseLectureModel>>> {
        return  withContext(Dispatchers.IO){
            flow {
                emit(Resource.Loading())
                val remoteLecture = try {
                    val response = lecturesInterface.getAllLecture()
                    response
                }
                catch (e:Exception){
                    null
                }
                remoteLecture?.let {networkLecture->
                    val convertedLecture = networkLecture.Lectures.map {
                        it.convertToLecture()
                    }
                    lectureDao.insertLecture(convertedLecture)
                    val lectures = lectureDao.getAllLectures(searchText = searchText)
                     emit(Resource.Success(lectures))
                    return@flow
                }
                try {
                    val localLecture = lectureDao.getAllLectures(searchText)
                    if (localLecture.isNotEmpty()){
                        emit(Resource.Success(localLecture))
                        
                    }
                    else{
                        emit(Resource.Error("Empty Database"))
                    }
                    return@flow
                }
                catch (e:IOException){
                    emit(Resource.Error("Database error"))
                    return@flow
                }
                catch (e:Exception){
                    emit(Resource.Error("Unknown Error"))
                    return@flow
                }
               
            }
        }
    }

    override suspend fun updateLecture(lecture: DatabaseLectureModel) {
        lectureDao.updateLecture(lecture)
    }

    override suspend fun deleteLecture(lecture: DatabaseLectureModel) {
        lectureDao.deleteLecture(lecture)
    }

    override suspend fun getSelectedLecture(lectureName: String): Flow<Resource<DatabaseLectureModel>> {
       return  withContext(Dispatchers.IO){
           flow {
               emit(Resource.Loading())
               val lecture = lectureDao.getSelectedLecture(lectureName = lectureName )
               if(lecture != null){
                   emit(Resource.Success(lecture))
               }
               else {
                   emit(Resource.Error("Lecture does not exists"))
               }

           }
       }

    }

    override suspend fun updateCategory(databaseCategoryModel: DatabaseCategoryModel) {
        TODO("Not yet implemented")
    }
// override suspend fun getSelected

    override suspend fun getAllCategory(): Flow<Resource<List<DatabaseCategoryModel>>> {
        return  withContext(Dispatchers.IO){
            flow {
                emit(Resource.Loading())
                val remoteCategories = try {
                    val response = lecturesInterface.getAllCategories()
                    response
                }
                catch (e:Exception){
                    null
                }
                remoteCategories?.let { remoteCategory ->
                    val convertedCategory = remoteCategory.map {
                        it.convertToCategory()
                    }
                    categoryDao.insertCategory(convertedCategory)
                    val categories = categoryDao.getALLCategory()
                    emit(Resource.Success(categories))
                    return@flow
                }
                
                try {
                    val categories = categoryDao.getALLCategory()
                    emit(Resource.Success(categories))
                    return@flow
                }
                catch (e:IOException){
                    emit(Resource.Error("Database error"))
                    return@flow
                }
                catch (e:Exception){
                    emit(Resource.Error("Unknown Error"))
                    return@flow
                }

            }
        }
    }
    

    override suspend fun deleteCategory(databaseCategoryModel: DatabaseCategoryModel) {
        categoryDao.deleteCategory(databaseCategoryModel)
    }

    override suspend fun getFavourites(): Flow<Resource<List<Favourite>>> {
        return  withContext(Dispatchers.IO){
            flow {
                emit(Resource.Loading())

                try {
                    val favourites = favouriteDao.getFavouriteLectures()
                    emit(Resource.Success(favourites))
                    return@flow
                }
                catch (e:IOException){
                    emit(Resource.Error("Database error"))
                    return@flow
                }
                catch (e:Exception){
                    emit(Resource.Error("Unknown Error"))
                    return@flow
                }

            }
        }
    }

    override suspend fun addFavourite(favourite: Favourite) {
        favouriteDao.addToFavourite(favourite)
    }

    override suspend fun deleteFavourite(lectureTitle: String) {
        favouriteDao.deleteFromFavourite(lectureTitle)
    }

    override suspend fun getRecentPlayed(): Flow<Resource<List<RecentlyPlayed>>> {
        return  withContext(Dispatchers.IO){
            flow {
                emit(Resource.Loading())

                try {
                    val recentlyPlayed =  recentPlayDao.getRecentPlayed()
                    emit(Resource.Success(recentlyPlayed))
                  return@flow
                }
                catch (e:IOException){
                    emit(Resource.Error("Database error"))
                    return@flow
                }
                catch (e:Exception){
                    emit(Resource.Error("Unknown Error"))
                    return@flow
                }

            }
        }
    }

    override suspend fun addRecentlyPlayed(recentlyPlayed: RecentlyPlayed) {
        recentPlayDao.insertRecentLecture(recentlyPlayed)
    }

    override suspend fun deleteRecent(recentlyPlayed: RecentlyPlayed) {
        recentPlayDao.deleteRecentPlayItem(recentlyPlayed)
    }

    override suspend fun getNewLecture(): Flow<Resource<List<NewlyAdded>>> {
        return  withContext(Dispatchers.IO){
            flow {
                emit(Resource.Loading())

                try {
                    val newLectures = newLecturesDao.getNewLectures()
                    emit(Resource.Success(newLectures))
                    return@flow
                }
                catch (e:IOException){
                    emit(Resource.Error("Database error"))
                    return@flow
                }
                catch (e:Exception){
                    emit(Resource.Error("Unknown Error"))
                    return@flow
                }

            }
        }
    }

    override suspend fun addNewLecture(newlyAdded: NewlyAdded) {
        newLecturesDao.insertNewLecture(newlyAdded)
    }

    override suspend fun deleteNewLecture(newlyAdded: NewlyAdded) {
        newLecturesDao.deleteNewLectures(newlyAdded)
    }

    override suspend fun getAllRequest(): List<WorkRequest> {
       return  workRequest.getAllRequest()
    }

    override suspend fun getRequest(lectureName: String): WorkRequest {
        return  workRequest.getRequest(lectureName)
    }

    override suspend fun insertRequest(newRequest: WorkRequest) {
        return workRequest.addRequest(newRequest)
    }



    override suspend fun deleteRequest(request:WorkRequest) {
        workRequest.removeWork(request)
    }

    override suspend fun deleteAllRequest() {
      workRequest.deleteAllRequest()
    }
}