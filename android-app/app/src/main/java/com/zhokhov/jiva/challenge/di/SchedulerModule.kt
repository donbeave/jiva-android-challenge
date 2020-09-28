package com.zhokhov.jiva.challenge.di

import com.zhokhov.jiva.challenge.data.MainSchedulerProvider
import com.zhokhov.jiva.challenge.data.SchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class SchedulerModule {

    @Binds
    abstract fun provideSchedulerProvider(storage: MainSchedulerProvider): SchedulerProvider

}
