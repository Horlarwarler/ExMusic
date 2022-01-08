package com.example.talimlectures.data.database

import androidx.room.Dao
import androidx.room.Query
import com.example.talimlectures.data.model.Lecture
import kotlinx.coroutines.flow.Flow


@Dao
interface LectureDao {
    @Query("SELECT * FROM lecturesTable ORDER BY id ASC" )
    fun getAllLectures(): Flow<List<Lecture>>
    @Query("SELECT * FROM lecturesTable WHERE title LIKE :searchQuery")
    fun searchLecture(searchQuery:String):Flow<List<Lecture>>
}