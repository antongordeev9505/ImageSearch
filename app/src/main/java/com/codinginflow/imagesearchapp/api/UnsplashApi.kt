package com.codinginflow.imagesearchapp.api

import com.codinginflow.imagesearchapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UnsplashApi {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        //get our api key from gradle.properties we added earlier
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }

    //add headers to http request
    //headers - metadata about request
    //get the requirments for headers from the API web site
    //version of APi and automatic authorizatioon
    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("/search/photos")
    //Query - parametres which we need from the API
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UnsplashResponse

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("/collections")
    suspend fun listOfCollections(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UnsplashCollectionResponse
}