package com.example.talimlectures.data.di

import android.content.Context
import androidx.room.Room
import com.example.talimlectures.data.local.database.*
import com.example.talimlectures.data.local.repository.LectureRepoInterface
import com.example.talimlectures.data.local.repository.LectureRepository
import com.example.talimlectures.domain.network.LecturesInterface
import com.example.talimlectures.util.Constant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides

    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        Database::class.java,
        DATABASE_NAME
    ).build()
    @Singleton
    @Provides
    fun provideLectureDao(database: Database) = database.provideLecture()
    @Singleton
    @Provides
    fun provideCategoryDao(database: Database) = database.provideCategory()
    @Singleton
    @Provides
    fun provideFavouriteDao(database: Database) = database.provideFavourite()
    @Singleton
    @Provides
    fun provideNewAddedDao(database: Database) = database.provideNewAdded()
    @Singleton
    @Provides
    fun provideRecentPlayedDao(database: Database) = database.provideRecent()
    @Singleton
    @Provides
    fun provideWorkRequestDao(database: Database) = database.provideWorkRequest()
    @Singleton
    @Provides
    fun provideslectureInterface(
        categoryDao: CategoryDao,
        lectureDao: LectureDao,
        favouriteDao: FavouriteDao,
        recentPlayDao: RecentPlayDao,
        workRequestDao: WorkRequestDao,
        lecturesInterface: LecturesInterface,
        newLecturesDao: NewLecturesDao

    ): LectureRepoInterface {
        return LectureRepository(
             categoryDao = categoryDao,
             lectureDao = lectureDao,
             favouriteDao = favouriteDao,
             recentPlayDao = recentPlayDao,
             workRequest = workRequestDao,
             lecturesInterface = lecturesInterface,
             newLecturesDao = newLecturesDao
         )
    }
}