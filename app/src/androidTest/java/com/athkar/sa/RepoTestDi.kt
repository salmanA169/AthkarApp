package com.athkar.sa

import com.athkar.sa.di.DIRepo
import com.athkar.sa.repo.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DIRepo::class])
abstract class RepoTestDi {
    @Singleton
    @Binds
    abstract fun bindFakeRepo(fakeRepo:FakeRepo):Repository
}