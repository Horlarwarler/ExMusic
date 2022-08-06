package com.example.talimlectures.data.di

import android.content.Context
import androidx.work.*
import com.example.talimlectures.data.local.repository.LectureRepoInterface
import com.example.talimlectures.domain.download.DownloadInterface
import com.example.talimlectures.domain.download.DownloadInterfaceImpl
import com.example.talimlectures.domain.music.MusicInterface
import com.example.talimlectures.domain.music.MusicPlayerImpl
import com.example.talimlectures.domain.network.LecturesInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun provideMusicInterface(): MusicInterface{
        return MusicPlayerImpl()
    }

   @Singleton
   @Provides
   fun providesDownloadInterface(
       @ApplicationContext context: Context,
       lectureRepoInterface: LectureRepoInterface
   ):DownloadInterface{
       return DownloadInterfaceImpl(context, lectureRepoInterface)
   }
}