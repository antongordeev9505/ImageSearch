package com.codinginflow.imagesearchapp.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

//Fragment should be injected themself using AndroidEntryPoint
//inject the field of the class
//this annot will inject VM here
@AndroidEntryPoint
//inflate layout here in constructor
class GalleryFragment: Fragment(R.layout.fragment_gallery){

    //viewmodel is injected by dagger
    private val viewModel by viewModels<GalleryViewModel>()

    //binding object - to access RV
    //when view will be destroyed we have to make _binding null again
    private var _binding: FragmentGalleryBinding? = null
    //to avoid using _binding always with safeoperator do next...
    //get _binding if it is even null
    //we can use this variable only between onCreateView and OnviewDestroyed
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //view is inflated layout in the fragment - fragment_gallery
        //instanciate bindng object
        _binding = FragmentGalleryBinding.bind(view)

        val adapter = UnsplashPhotoAdapter()

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
        }

        //viewLifecycleOwner - lifecycle of fragments view
        //we want to stop updating ui if the view of the fragment is destroid
        //this can happen only while fragment instance is still in memory (fragment put in BackStack)
        //in cases of detached Fragments, the lifecycle of the Fragment can be considerably longer than the lifecycle of the View itself
        viewModel.photos.observe(viewLifecycleOwner) {
            //use lificycle of the Fragment`s view - ussualy we need it
            adapter.submitData(viewLifecycleOwner.lifecycle, it)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}