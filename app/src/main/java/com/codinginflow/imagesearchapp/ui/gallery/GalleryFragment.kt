package com.codinginflow.imagesearchapp.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.data.UnsplashPhoto
import com.codinginflow.imagesearchapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

//Fragment should be injected themself using AndroidEntryPoint
//inject the field of the class
//this annot will inject VM here
@AndroidEntryPoint
//inflate layout here in constructor
class GalleryFragment: Fragment(R.layout.fragment_gallery), UnsplashPhotoAdapter.OnItemClickListener{

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

        //pass the fragment as the listener to the adapter
        val adapter = UnsplashPhotoAdapter(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            //just put it here to turn off animation in RV
            //it`s ok till we change our items
            recyclerView.itemAnimator = null
            //concatinate 2 adapters
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                //declare that we have to forward retry function
                header = UnsplashPhotoLoadStateAdapter{ adapter.retry() }, //retry functionality - retry load of another page
                footer = UnsplashPhotoLoadStateAdapter{ adapter.retry() },
            )
            retryButton.setOnClickListener {
                adapter.retry()
            }
        }

        //viewLifecycleOwner - lifecycle of fragments view
        //we want to stop updating ui if the view of the fragment is destroid
        //this can happen only while fragment instance is still in memory (fragment put in BackStack)
        //in cases of detached Fragments, the lifecycle of the Fragment can be considerably longer than the lifecycle of the View itself
        viewModel.photos.observe(viewLifecycleOwner) {
            //use lificycle of the Fragment`s view - ussualy we need it
            adapter.submitData(viewLifecycleOwner.lifecycle, it)

        }

        //show us the loadState (refresh data set)
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                //when the list is refreshing with new data set
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                //loading is finished and the state is not error
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                //if there is not internet connection
                retryButton.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    //if there si not more date to load
                        loadState.append.endOfPaginationReached &&
                        adapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }

        }

        //activate menu
        setHasOptionsMenu(true)
    }

    override fun onItemClick(photo: UnsplashPhoto) {
        //have to rebuild project to have access to navigation.safeargs which are generated
        //earlier we declared UnsplashPhoto as the type which we can forward to another fragment
        //create action
        //send parameter in compile time safe way
        //it`s better that put it in bundle cuz bundle isn`t which is not compiletime sage
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list_of_collections -> {
                displayListOfCollections()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayListOfCollections() {
//        Toast.makeText(context, "Users", Toast.LENGTH_SHORT).show()
        findNavController().navigate(GalleryFragmentDirections.actionGalleryFragmentToListOfCollectionsFragment())
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //activate menu for this fragment
        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        //Callbacks for changes to the query text.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //execute search when we press search button
                if (query != null)  {
                    binding.recyclerView.scrollToPosition(0) // scroll to the top
                    viewModel.searchPhotos(query)
                    //hide the keyboard
                    searchView.clearFocus()
                }
                //we made all logic and return true - we handled submit button
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //execute search while we are typing
                //don`t use it
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}