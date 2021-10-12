package com.codinginflow.imagesearchapp.ui.collections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.databinding.FragmentListOfCollectionsBinding
import com.codinginflow.imagesearchapp.ui.gallery.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectionsFragment : Fragment(R.layout.fragment_list_of_collections) {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentListOfCollectionsBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentListOfCollectionsBinding.bind(view)

        val adapter = UnsplashCollectionAdapter()

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
        }

        viewModel.collections.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}