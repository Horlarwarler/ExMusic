package com.example.talimlectures.data.database

import androidx.room.*
import com.example.talimlectures.data.model.*
import kotlinx.coroutines.flow.Flow


@Dao
interface LectureDao {
    //Get all lectures
    @Query("SELECT * FROM lecturesTable ORDER BY lectureId ASC" )
    fun getAllLectures(): Flow<List<Lecture>>

    //Get lectures by category
    @Query("SELECT * FROM lecturesTable WHERE categoryId = :categoryId ORDER BY lectureId ASC")
    fun getLectureByCategory(categoryId:Int):Flow<List<Lecture>>

    // Search Lecture
    @Query("SELECT * FROM lecturesTable WHERE lectureTitle LIKE :searchQuery")
    fun searchLecture(searchQuery:String):Flow<List<Lecture>>

    //Getting Favourite lectures
//    @Query("SELECT * FROM lecturesTable WHERE favourite = 1 " )
//    fun favouriteLecture():Flow<List<Lecture>>

    // Inserting lecture into local database
    @Insert
    suspend fun  insertLecture(lecture: Lecture)

    //Getting selected lecture
    @Query("SELECT * FROM lecturesTable WHERE lectureId = :lectureId")
    fun getSelectedLecture(lectureId:Int):Flow<Lecture>

    // Updating lecture in the local database such as favourite
    @Update
    suspend fun  updateLecture(lecture: Lecture)

    //Deleting lecture
    @Delete
    suspend fun  deleteLecture(lecture: Lecture)

}
@Dao
interface  CategoryDao{
    @Query("SELECT * FROM category ORDER BY categoryId ASC")
    fun getALLCategory():Flow<List<Category>>
    //Getting selected Category
    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    fun getSelectedCategory(categoryId: Int):Flow<Category>
    @Insert
    suspend fun  insertCategory(category: Category)
    @Delete
    suspend fun  deleteCategory(category: Category)
    @Update
    suspend fun  updateCategory(category: Category)

}
@Dao
interface  RecentPlayDao{
    @Query("SELECT * FROM recentlyPlayed ORDER BY id ASC")
    fun getRecentPlayed():Flow<List<RecentlyPlayed>>

    //Getting selected Recent Played
    @Query("SELECT * FROM recentlyPlayed WHERE id = :idSelected")
    fun getSelectedRecentPlayed(idSelected:Int):Flow<RecentlyPlayed>

    @Insert
    suspend fun  insertRecentLecture(recentlyPlayed: RecentlyPlayed)
    @Insert
    suspend fun  deleteRecentPlayItem(recentlyPlayed: RecentlyPlayed)
}

@Dao
interface  FavouriteDao{
    @Query("SELECT * FROM favouriteTable ORDER BY id ASC")
    fun getFavouriteLectures():Flow<List<Favourite>>

    //Getting selected Recent Played
    @Query("SELECT * FROM favouriteTable WHERE id = :idSelected")
    fun getSelectedFavouriteLecture(idSelected: Int):Flow<Favourite>

    @Insert
    suspend fun  addToFavourite(favourite: Favourite)
    @Insert
    suspend fun  deleteFromFavourite(favourite: Favourite)
}
@Dao
interface  NewLecturesDao{
    @Query("SELECT * FROM newlyAdded ORDER BY id ASC")
    fun getNewLectures():Flow<List<NewlyAdded>>
    //Getting selected NewLectures
    @Query("SELECT * FROM newlyAdded WHERE id = :idSelected")
    fun getSelectedNewLectures(idSelected: Int):Flow<NewlyAdded>
    @Insert
    suspend fun  insertNewLecture(newlyAdded: NewlyAdded)
    @Delete
    suspend fun deleteNewLectures(newlyAdded: NewlyAdded)
}