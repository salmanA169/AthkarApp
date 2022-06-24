package com.athkar.sa.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.athkar.sa.Constants
import com.athkar.sa.db.AthkarDatabase
import com.athkar.sa.repo.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleSingleton {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context:Context)= Room.databaseBuilder(
        context,AthkarDatabase::class.java,Constants.DATABASE_NAME
    ).build()

}