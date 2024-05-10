package com.athkar.sa.di

import com.athkar.sa.coroutine.DispatcherProvider
import com.athkar.sa.coroutine.DispatcherProviderImpl
import com.athkar.sa.repo.Repository
import com.athkar.sa.repo.RepositoryImpl
import com.athkar.sa.repo.quran.QuranRepository
import com.athkar.sa.repo.quran.QuranRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DIRepo {

    @Binds
    @Singleton
    abstract fun bindRepo(repoImpl: RepositoryImpl): Repository


    @Binds
    abstract fun bindQuranRepo(
        quranRepositoryImpl: QuranRepositoryImpl
    ): QuranRepository

    @Binds
    abstract fun bindDispatcherProvider(
        dispatcherProviderImpl: DispatcherProviderImpl
    ): DispatcherProvider


}