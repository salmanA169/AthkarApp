package com.athkar.sa.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.util.JsonReader
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.WorkManager
import com.athkar.sa.Constants
import com.athkar.sa.db.AthkarDatabase
import com.athkar.sa.db.RoomCallback
import com.athkar.sa.db.entity.Athkar
import com.athkar.sa.remote.CalenderApi
import com.athkar.sa.uitls.dataStoreSettings
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.InputStream
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleSingleton {

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context) =
        context.getSystemService(NotificationManager::class.java)

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context) =
        context.getSystemService(AlarmManager::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context,callback: RoomCallback) = Room.databaseBuilder(
        context, AthkarDatabase::class.java, Constants.DATABASE_NAME
    ).addCallback(callback).build()

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