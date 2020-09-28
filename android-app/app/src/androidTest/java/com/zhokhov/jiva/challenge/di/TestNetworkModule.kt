package com.zhokhov.jiva.challenge.di

import com.zhokhov.jiva.challenge.data.http.ApiService
import com.zhokhov.jiva.challenge.data.http.FakeApiService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class TestNetworkModule {

    @Binds
    abstract fun provideApiService(apiService: FakeApiService): ApiService

}
