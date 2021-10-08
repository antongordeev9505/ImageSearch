package com.codinginflow.imagesearchapp.di

import com.codinginflow.imagesearchapp.api.UnsplashApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

//here we give instructions how to create objects we need
//Module - instructions for dagger
// @InstallIn in what scope we want to use these objects from Module
// ApplicationComponent - scope of App

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    //instance of retrofit
    //@Provide - how to create object
    @Provides
    //only one objects
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit) =
        retrofit.create(UnsplashApi::class.java)
}