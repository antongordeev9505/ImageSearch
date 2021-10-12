package com.codinginflow.imagesearchapp.data

import androidx.paging.PagingSource
import com.codinginflow.imagesearchapp.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

//we will instanciate the class by ourself(we will not inject this class)
//cuz there is property query we get only in runtime, we don`t have it in compile time
class UnsplashPagingSourceCollection(
    private val unsplashApi: UnsplashApi
): PagingSource<Int, UnsplashCollection>() {

    //turn the data into pages
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashCollection> {
        //current page, if the page is loaded first time key has null cuz we need 1
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            //loadsize will configure later
            val response = unsplashApi.listOfCollections(position, params.loadSize)
            val collections = response

            //constract the page which we will return
            //later we will fit it to RV adapter
            LoadResult.Page(
                data = collections,
                //it might be first page
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                //it might be last page
                nextKey = if (collections.isEmpty()) null else position + 1
            )
        } catch (error: IOException) {
            //when no internet
            LoadResult.Error(error)

        } catch (error: HttpException) {
            //problem on the server
            LoadResult.Error(error)
        }
    }
}