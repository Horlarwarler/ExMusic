package com.crezent.common.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.crezent.common.preference.ExMusicPreference
import com.crezent.common.preference.Preference
import com.crezent.common.util.FileHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {



    @Provides
    @Singleton
    fun providesPreferences(
        app: Application
    ): Preference {
        val sharedPreference = app.getSharedPreferences("prefs",MODE_PRIVATE)
        return ExMusicPreference(
            sharedPreferences = sharedPreference
        )
    }

    @Provides
    @Singleton
    fun providesFileHelper(
        context:Context
    ):FileHelper{
        return FileHelper(context)
    }
}