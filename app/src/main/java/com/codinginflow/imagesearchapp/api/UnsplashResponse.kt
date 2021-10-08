package com.codinginflow.imagesearchapp.api

import com.codinginflow.imagesearchapp.data.UnsplashPhoto

//what we get from API
data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)