package com.example.talimlectures.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.talimlectures.data.model.Lecture

@Database(entities = [Lecture::class], version = 1, exportSchema = false)
abstract  class Database():RoomDatabase(){
    abstract fun provideLecture(): LectureDao
}