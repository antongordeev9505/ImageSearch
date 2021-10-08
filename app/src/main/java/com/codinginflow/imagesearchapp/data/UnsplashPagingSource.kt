package com.codinginflow.imagesearchapp.data

import androidx.paging.PagingSource
import com.codinginflow.imagesearchapp.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

//we will instanciate the class by ourself(we will not inject this class)
//cuz there is property query we get only in runtime, we don`t have it in compile time
class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
    //int - distingush between pages
    //UnsplashPhoto - object that will fill pages
): PagingSource<Int, UnsplashPhoto>() {

    //turn the data into pages
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        //current page, if the page is loaded first time key has null cuz we need 1
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            //loadsize will configure later
            val response = unsplashApi.searchPhotos(query, position, params.loadSize)
            val photos = response.results

            //constract the page which we will return
            //later we will fit it to RV adapter
            LoadResult.Page(
                data = photos,
                //it might be first page
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                //it might be last page
                nextKey = if (photos.isEmpty()) null else position + 1
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