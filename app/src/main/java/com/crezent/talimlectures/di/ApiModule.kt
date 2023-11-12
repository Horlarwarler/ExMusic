package com.crezent.talimlectures.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
 private const val BASE_URL = "https://api.jsonbin.io/"
 private const val API_KEY = "\$2b\$10\$jMmXY2YwBdar.LKrkg1NCOG9z7ARus5SkE1o4e0kdHy8j4Vf6vtDO"


 @Singleton
 @Provides
 fun providesDataStore(
  @ApplicationContext context: Context
 ): DataStore<Preferences> {
  return PreferenceDataStoreFactory.create(
   corruptionHandler = ReplaceFileCorruptionHandler(
    produceNewData = { emptyPreferences() }
   ),
   produceFile = { context.preferencesDataStoreFile("USER_PREFERENCE") }
  )
 }
 @Singleton
 @Provides
 fun providesImageLoader(
  context: Context
 ):ImageLoader{
  val imageLoader = ImageLoader(
   context = context
  )
  return  imageLoader
 }
}



