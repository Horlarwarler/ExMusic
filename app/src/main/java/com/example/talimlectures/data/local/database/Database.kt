package com.example.talimlectures.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.talimlectures.data.model.*

@Database(entities = [DatabaseLectureModel::class,DatabaseCategoryModel::class,Favourite::class,RecentlyPlayed::class,NewlyAdded::class, WorkRequest::class], version = 1, exportSchema = false)
abstract  class Database():RoomDatabase(){
    abstract fun provideLecture(): LectureDao
    abstract fun provideCategory():CategoryDao
    abstract fun provideFavourite():FavouriteDao
    abstract  fun provideRecent():RecentPlayDao
    abstract  fun provideNewAdded():NewLecturesDao
    abstract fun provideWorkRequest():WorkRequestDao
}