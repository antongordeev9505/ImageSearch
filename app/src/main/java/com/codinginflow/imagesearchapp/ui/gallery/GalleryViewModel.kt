package com.codinginflow.imagesearchapp.ui.gallery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.codinginflow.imagesearchapp.data.UnsplashRepository

//ViewModelInject - the same Injects but for VM
class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {

    //variable for query that user will type in
    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

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
        //create default query
        private const val DEFAULT_QUERY = "cats"
    }
}