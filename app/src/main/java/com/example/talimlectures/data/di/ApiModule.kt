package com.example.talimlectures.data.di

import com.example.talimlectures.domain.network.LecturesInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
 private const val   BASE_URL = "https://api.jsonbin.io/"
 private const val API_KEY = "\$2b\$10\$jMmXY2YwBdar.LKrkg1NCOG9z7ARus5SkE1o4e0kdHy8j4Vf6vtDO"

 @Singleton
 @Provides
 fun provideRetrofit(): Retrofit = Retrofit.Builder()
  .addConverterFactory(GsonConverterFactory.create())
  .baseUrl(BASE_URL)
  .build()

 @Singleton
 @Provides
 fun providesLectureInterface(retrofit: Retrofit) = retrofit.create(LecturesInterface::class.java)


}