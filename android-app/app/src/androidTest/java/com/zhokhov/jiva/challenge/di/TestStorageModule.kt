package com.zhokhov.jiva.challenge.di

import com.zhokhov.jiva.challenge.data.storage.FakeStorage
import com.zhokhov.jiva.challenge.data.storage.Storage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class TestStorageModule {

    @Binds
    abstract fun provideStorage(storage: FakeStorage): Storage

}
