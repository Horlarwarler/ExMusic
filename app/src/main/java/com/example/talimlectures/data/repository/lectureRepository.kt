package com.example.talimlectures.data.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.talimlectures.data.database.*
import com.example.talimlectures.data.model.*
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class lectureRepository @Inject constructor(
    private val lectureDao: LectureDao,
    private val recentPlayDao: RecentPlayDao,
    private val categoryDao: CategoryDao,
    private val favouriteDao: FavouriteDao,
    private val newLecturesDao: NewLecturesDao,
) {
    //Lecture Sections
    // To get all lectures
    val allLecture:Flow<List<Lecture>> = lectureDao.getAllLectures()
    // get lectures by Category
    fun getLectureByCategory(categoryID:Int) = lectureDao.getLectureByCategory(categoryId = categoryID)
    // Searching  for lecture
    fun searchLecture(searchQuery:String):Flow<List<Lecture>> = lectureDao.searchLecture(searchQuery = searchQuery)
    //Get selected Lecture
    fun getSelectedLecture(lectureId:Int):Flow<Lecture> = lectureDao.getSelectedLecture(lectureId = lectureId)
    //Getting favourite lectures
   // val favouriteLecture:Flow<List<Lecture>> = lectureDao.favouriteLecture()
    //Adding new Lectures
    suspend fun addLecture(lecture: Lecture):Unit = lectureDao.insertLecture(lecture = lecture)
    // Update lecture
    suspend fun updateLecture(lecture: Lecture):Unit = lectureDao.updateLecture(lecture = lecture)
    //Delete lecture
    suspend fun deleteLecture(lecture: Lecture):Unit = lectureDao.deleteLecture(lecture = lecture)

    //Category Sections
    //Get all category
    val allCategory:Flow<List<Category>> = categoryDao.getALLCategory()
    //Get selectedCategory
    fun getSelectedCategory(categoryID: Int) = categoryDao.getSelectedCategory(categoryId = categoryID)
    //Insert into category
    suspend fun  insertCategory(category: Category) = categoryDao.insertCategory(category = category)
    suspend fun  deleteCategory(category: Category) = categoryDao.deleteCategory(category = category)
    suspend fun  updateCategory(category: Category) = categoryDao.updateCategory(category = category)

    // Favourite Sections
    val allFavourite:Flow<List<Favourite>> = favouriteDao.getFavouriteLectures()
    //Get selectedFavourite
    fun getSelectedFavourite(selectedFavourite:Int) = favouriteDao.getSelectedFavouriteLecture(idSelected = selectedFavourite)
    //Insert into Favourite
    suspend fun  addToFavourite(favourite: Favourite) = favouriteDao.addToFavourite(favourite = favourite)
    //Delete from Favourite
    suspend fun  deleteFromFavourite(favourite: Favourite) = favouriteDao.deleteFromFavourite(favourite = favourite)
    //New Lectures Section
    val allNewLectures:Flow<List<NewlyAdded>> = newLecturesDao.getNewLectures()
    fun getSelectedNewLectures(idSelected: Int):Flow<NewlyAdded> = newLecturesDao.getSelectedNewLectures(idSelected = idSelected)

    suspend fun  insertNewLecture(newlyAdded: NewlyAdded) = newLecturesDao.insertNewLecture(newlyAdded = newlyAdded)

    suspend fun deleteNewLectures(newlyAdded: NewlyAdded) = newLecturesDao.deleteNewLectures(newlyAdded = newlyAdded)

    val allRecentPlayed:Flow<List<RecentlyPlayed>> =  recentPlayDao.getRecentPlayed()

    //Getting selected Recent Played

    fun getSelectedRecentPlayed(idSelected:Int):Flow<RecentlyPlayed> = recentPlayDao.getSelectedRecentPlayed(idSelected = idSelected)

    suspend fun  insertRecentLecture(recentlyPlayed: RecentlyPlayed) = recentPlayDao.insertRecentLecture(recentlyPlayed = recentlyPlayed)

    suspend fun  deleteRecentPlayItem(recentlyPlayed: RecentlyPlayed) = recentPlayDao.deleteRecentPlayItem(recentlyPlayed = recentlyPlayed)
}