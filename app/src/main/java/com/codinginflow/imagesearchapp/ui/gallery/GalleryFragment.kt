package com.codinginflow.imagesearchapp.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codinginflow.imagesearchapp.R
import dagger.hilt.android.AndroidEntryPoint

//Fragment should be injected themself using AndroidEntryPoint
//inject the field of the class
//this annot will inject VM here
@AndroidEntryPoint
//inflate layout here in constructor
class GalleryFragment: Fragment(R.layout.fragment_gallery){

    //viewmodel is injected by dagger
    private val viewModel by viewModels<GalleryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get here paging data type of UnsplashPhoto

        //viewLifecycleOwner - lifecycle of fragments view
        //we want to stop updating ui if the view of the fragment is destroid
        //this can happen only while fragment instance is still in memory (fragment put in BackStack)
        //in cases of detached Fragments, the lifecycle of the Fragment can be considerably longer than the lifecycle of the View itself
        viewModel.photos.observe(viewLifecycleOwner) {

        }
    }
}