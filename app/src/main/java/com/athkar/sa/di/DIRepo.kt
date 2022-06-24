package com.athkar.sa.di

import com.athkar.sa.repo.Repository
import com.athkar.sa.repo.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DIRepo {

    @Binds
    @Singleton
    abstract fun bindRepo(repoImpl:RepositoryImpl):Repository
}