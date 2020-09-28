package com.zhokhov.jiva.challenge.di

import com.zhokhov.jiva.challenge.data.storage.FakeStorage
import com.zhokhov.jiva.challenge.data.storage.Storage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class TestStorageModule {

    @Binds
    abstract fun provideStorage(storage: FakeStorage): Storage

}
