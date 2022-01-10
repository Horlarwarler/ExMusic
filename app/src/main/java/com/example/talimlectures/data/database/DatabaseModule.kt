package com.example.talimlectures.data.database

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
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
    )
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

}