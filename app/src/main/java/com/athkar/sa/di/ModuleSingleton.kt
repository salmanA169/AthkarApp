package com.athkar.sa.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.net.ConnectivityManager
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.room.Room
import androidx.work.WorkManager
import com.athkar.sa.Constants
import com.athkar.sa.db.AthkarDatabase
import com.athkar.sa.db.RoomCallback
import com.athkar.sa.remote.CalenderApi
import com.athkar.sa.remote.QuranApi
import com.athkar.sa.service.PlaybackService
import com.athkar.sa.uitls.dataStoreSettings
import com.athkar.sa.uitls.quranSettingsDataStore
import com.google.android.gms.location.LocationServices
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleSingleton {

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager =
        context.getSystemService(NotificationManager::class.java)

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager =
        context.getSystemService(AlarmManager::class.java)

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context):ConnectivityManager =
        context.getSystemService(ConnectivityManager::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context, callback: RoomCallback) =
        Room.databaseBuilder(
            context, AthkarDatabase::class.java, Constants.DATABASE_NAME
        ).fallbackToDestructiveMigration().addCallback(callback).build()

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideFusedLocationService(@ApplicationContext context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    @CalendarApiProvider
    fun provideRetrofit() = Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create<CalenderApi>()

    @Provides
    @Singleton
    @QuranAudioApiProvider
    fun provideQuranApi() = Retrofit.Builder().baseUrl(Constants.QURAN_BASE_URI)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create<QuranApi>()

    @Provides
    @Singleton
    @SettingsDataStore
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStoreSettings

    @Provides
    @Singleton
    @QuranSettingsDataStore
    fun provideSettingsQuranDataStore(@ApplicationContext context: Context) =
        context.quranSettingsDataStore


    @Provides
    @Singleton
    fun provideStorage() = Firebase.storage


}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class QuranAudioApiProvider

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CalendarApiProvider


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SettingsDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class QuranSettingsDataStore