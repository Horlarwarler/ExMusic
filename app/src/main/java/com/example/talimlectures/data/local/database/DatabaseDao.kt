package com.example.talimlectures.data.local.database

import androidx.room.*
import com.example.talimlectures.data.model.*
import com.example.talimlectures.data.model.WorkRequest


@Dao
interface LectureDao {
    //Get all lectures


    //Get lectures by category
    @Query("""
         SELECT * 
        FROM lecturesTable 
        WHERE lower(lectureTitle) LIKE '%' || lower(:searchText)  || '%' ORDER BY lectureId ASC""")

    suspend fun getAllLectures( searchText:String):List<DatabaseLectureModel>

    // Search Lecture
//    @Query("SELECT * FROM lecturesTable WHERE lectureTitle LIKE :searchQuery")
//    fun searchLecture(searchQuery:String):Flow<List<Lecture>>

    //Getting Favourite lectures
    @Query("SELECT * FROM favouriteTable " )
    suspend fun favouriteLecture():List<Favourite>

    // Inserting lecture into local database
    @Insert
    suspend fun  insertLecture(lecture: List<DatabaseLectureModel>)

    //Getting selected lecture
    @Query("SELECT * FROM lecturesTable WHERE lectureTitle = :lectureName")
    suspend fun getSelectedLecture(lectureName:String):DatabaseLectureModel?

    // Updating lecture in the local database such as favourite
    @Update
    suspend fun  updateLecture(lecture: DatabaseLectureModel)

    //Deleting lecture
    @Delete
    suspend fun  deleteLecture(lecture: DatabaseLectureModel)

}
@Dao
interface  CategoryDao{
    @Query("SELECT * FROM category ORDER BY categoryId ASC")
    suspend fun getALLCategory():List<DatabaseCategoryModel>
    //Getting selected Category

    @Insert
    suspend fun  insertCategory(databaseCategoryModel: List<DatabaseCategoryModel>)
    @Delete
    suspend fun  deleteCategory(databaseCategoryModel: DatabaseCategoryModel)
    @Update
    suspend fun  updateCategory(databaseCategoryModel: DatabaseCategoryModel)

}
@Dao
interface  RecentPlayDao{
    @Query("SELECT * FROM recentlyPlayed ORDER BY id DESC")
    suspend fun getRecentPlayed():List<RecentlyPlayed>

    //Getting selected Recent Played

    @Insert
    suspend fun  insertRecentLecture(recentlyPlayed: RecentlyPlayed)
    @Delete
    suspend fun  deleteRecentPlayItem(recentlyPlayed: RecentlyPlayed)
}

@Dao
interface  FavouriteDao{
    @Query("SELECT * FROM favouriteTable ORDER BY id ASC")
    suspend fun getFavouriteLectures():List<Favourite>

    //Getting selected Recent Played

    @Insert
    suspend fun  addToFavourite(favourite: Favourite)
    @Query("DELETE  FROM favouriteTable WHERE lectureTitle = :lectureTitle  ")
    suspend fun  deleteFromFavourite(lectureTitle: String)
}
@Dao
interface  NewLecturesDao{
    @Query ("SELECT * FROM newlyAdded ORDER BY id ASC")
    suspend fun getNewLectures():List<NewlyAdded>
    //Getting selected NewLectures
    @Insert
    suspend fun  insertNewLecture(newlyAdded: NewlyAdded)
    @Delete
    suspend fun deleteNewLectures(newlyAdded: NewlyAdded)
}

@Dao
interface  WorkRequestDao{
    @Query( "SELECT * FROM workRequest ORDER BY id ASC")
    suspend fun getAllRequest():List<WorkRequest>
    @Query("SELECT * FROM workRequest  WHERE lectureName = :lectureName ")
    suspend fun getRequest(lectureName: String): WorkRequest
    @Insert
    suspend fun addRequest(workRequest: WorkRequest)
    @Delete
    suspend fun removeWork(workRequest: WorkRequest)
    @Query ("DELETE  FROM workRequest")
    suspend fun deleteAllRequest()
}