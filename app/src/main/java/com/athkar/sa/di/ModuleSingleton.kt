package com.athkar.sa.di

import android.content.Context
import androidx.room.Room
import com.athkar.sa.Constants
import com.athkar.sa.db.AthkarDatabase
import com.athkar.sa.remote.CalenderApi
import com.athkar.sa.uitls.dataStoreSettings
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleSingleton {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, AthkarDatabase::class.java, Constants.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create<CalenderApi>()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStoreSettings

}