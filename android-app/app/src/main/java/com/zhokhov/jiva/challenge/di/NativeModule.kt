package com.zhokhov.jiva.challenge.di

import com.zhokhov.jiva.challenge.utils.Base64
import com.zhokhov.jiva.challenge.utils.NativeBase64
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class NativeModule {

    @Binds
    abstract fun provideBase64(base64: NativeBase64): Base64

}