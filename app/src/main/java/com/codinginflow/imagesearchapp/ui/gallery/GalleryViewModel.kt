package com.codinginflow.imagesearchapp.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.codinginflow.imagesearchapp.data.UnsplashRepository

//ViewModelInject - the same Injects but for VM
class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    //SavedStateHandle - let us save piece of data throw proccess death
    //Assisted - let dagger inject new variable

    //variable for query that user will type in
    //currentQuery will be persistant throw proc death
    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    //we will observe photos in fragment
    val photos = currentQuery.switchMap { queryString ->
        //lambda will be executed once currentQuery will be changed
        //cachedIn- cache data in case of rotating device
        //we need it cuz we can`t load from the same page twice
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    //get query from the fragment
    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    companion object {
        //key for persisting data throw proc death
        private const val CURRENT_QUERY = "current_query_key"
        //create default query
        private const val DEFAULT_QUERY = "cats"
    }
}