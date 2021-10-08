package com.codinginflow.imagesearchapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//HiltAndroidApp - generate automatically code for hilt
//we need this class if we use dagger hilt
//activate this class in manifest
@HiltAndroidApp
class ImageSearchApplication: Application() {
}