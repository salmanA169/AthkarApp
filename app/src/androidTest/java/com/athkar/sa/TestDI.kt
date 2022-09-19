package com.athkar.sa

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.athkar.sa.db.AthkarDatabase
import com.athkar.sa.di.ModuleSingleton
import com.athkar.sa.remote.CalenderApi
import com.athkar.sa.uitls.dataStoreSettings
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [ModuleSingleton::class])
object TestDI {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, AthkarDatabase::class.java).build()

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideFusedLocationService(@ApplicationContext context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create<CalenderApi>()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStoreSettings



}