package com.codinginflow.imagesearchapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.codinginflow.imagesearchapp.api.UnsplashApi
import javax.inject.Inject
import javax.inject.Singleton

//Inject - tells dagger how to create Repo class
//and to inject unsplashApi (forward UnsplashApi on module)
//every classses which we created by ourself should have Inject constructor
@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi) {

    fun getSearchResults(query: String) =
        //Pager will use PagerSource to create paging data
        Pager(
            //configure loading
            config = PagingConfig(
                //number of items loaded at once
                pageSize = 20,
                //number of items that may be loaded into PagingData before pages should be dropped
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSource(unsplashApi, query) }
        //turn pager to a stream of paging data
        //we will observ it in fragment
        ).liveData

    fun getListOfCollections() =
        //Pager will use PagerSource to create paging data
        Pager(
            //configure loading
            config = PagingConfig(
                //number of items loaded at once
                pageSize = 20,
                //number of items that may be loaded into PagingData before pages should be dropped
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSourceCollection(unsplashApi) }
            //turn pager to a stream of paging data
            //we will observ it in fragment
        ).liveData
}