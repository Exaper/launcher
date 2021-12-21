package com.exaper.launcher.di

import com.exaper.launcher.api.PolicyApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class PolicyApiClientModule {
    @Provides
    fun providePolicyApiService(): PolicyApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.jsonbin.io")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(PolicyApiService::class.java)
    }
}