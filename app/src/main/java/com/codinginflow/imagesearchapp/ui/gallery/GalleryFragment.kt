package com.codinginflow.imagesearchapp.ui.gallery

import androidx.fragment.app.Fragment
import com.codinginflow.imagesearchapp.R
import dagger.hilt.android.AndroidEntryPoint

//Fragment should be injected themself using AndroidEntryPoint
//inject the field of the class
//this annot will inject VM here
@AndroidEntryPoint
//inflate layout here in constructor
class GalleryFragment: Fragment(R.layout.fragment_gallery){
}