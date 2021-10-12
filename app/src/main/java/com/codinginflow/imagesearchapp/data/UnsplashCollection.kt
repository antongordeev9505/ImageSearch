package com.codinginflow.imagesearchapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnsplashCollection(
    val cover_photo: CoverPhoto,
    val id: String,
    val title: String,
    val total_photos: Int
): Parcelable {

    @Parcelize
    data class CoverPhoto(
        val urls: Urls,
        val user: User
    ): Parcelable {

        @Parcelize
        data class Urls(
            val full: String,
            val raw: String,
            val regular: String,
            val small: String,
            val thumb: String
        ): Parcelable

        @Parcelize
        data class User(
            val name: String,
            val username: String
        ): Parcelable {
            val attributionUrl get() = "https://unsplash.com/$username?utm_source=ImageSearchApp&utm_medium=referral"
        }
    }
}