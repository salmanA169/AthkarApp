package com.athkar.sa.di

import com.athkar.sa.quran.QuranFileImpl
import com.athkar.sa.quran.QuranFileManager
import com.athkar.sa.quran.QuranInfo
import com.athkar.sa.quran.QuranInfoImpl
import com.athkar.sa.quran.data.QuranDataSource
import com.athkar.sa.quran.data.QuranDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class QuranBindModule {

    @Binds
    abstract fun bindQuranInfo(
        quranInfoDataSourceImpl: QuranInfoImpl
    ): QuranInfo

    @Binds
    abstract fun bindQuranData(
        quranDataImpl: QuranFileImpl
    ): QuranFileManager

    @Binds
    abstract fun bindQuranDataSource(
        quranDataSourceImpl: QuranDataSourceImpl
    ): QuranDataSource

}