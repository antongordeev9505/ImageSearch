package com.codinginflow.imagesearchapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//parcelize - the way to pass parcelable object between activities or fragments
@Parcelize
data class UnsplashPhoto(
    val description: String?,
    val id: String,
    val urls: UnsplashPhotoUrls,
    val user: UnsplashUser
): Parcelable {

    @Parcelize
    data class UnsplashPhotoUrls(
        val full: String,
        val raw: String,
        val regular: String,
        val small: String,
        val thumb: String
    ): Parcelable

    @Parcelize
    data class UnsplashUser(
        val name: String,
        val username: String
    ): Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=ImageSearchApp&utm_medium=referral"
    }
}