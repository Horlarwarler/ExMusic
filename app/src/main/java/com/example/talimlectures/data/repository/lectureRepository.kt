package com.example.talimlectures.data.repository

import com.example.talimlectures.data.database.LectureDao
import com.example.talimlectures.data.model.Lecture
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class lectureRepository @Inject constructor(val lectureDao: LectureDao) {
    // To get all lectures
    val allLecture:Flow<List<Lecture>> = lectureDao.getAllLectures()
    // Searching  for lecture
    fun searchLecture(searchQuery:String):Flow<List<Lecture>> = lectureDao.searchLecture(searchQuery = searchQuery)
}