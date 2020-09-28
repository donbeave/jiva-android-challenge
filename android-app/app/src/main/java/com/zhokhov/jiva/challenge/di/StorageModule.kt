package com.zhokhov.jiva.challenge.di

import com.zhokhov.jiva.challenge.data.storage.SharedPreferencesStorage
import com.zhokhov.jiva.challenge.data.storage.Storage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class StorageModule {

    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage

}
