package com.codinginflow.imagesearchapp.data

import com.codinginflow.imagesearchapp.api.UnsplashApi
import javax.inject.Inject
import javax.inject.Singleton

//Inject - tells dagger how to create Repo class
//and to inject unsplashApi (forward UnsplashApi on module)
//every classses which we created by ourself should have Inject constructor
@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi) {
}