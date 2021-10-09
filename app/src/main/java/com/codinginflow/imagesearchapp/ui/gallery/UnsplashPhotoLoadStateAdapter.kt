package com.codinginflow.imagesearchapp.ui.gallery

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.imagesearchapp.databinding.UnsplashPhotoLoadStateFooterBinding

//Adapter for displaying a RecyclerView item based on LoadState, such as a loading spinner, or a retry error button.
class UnsplashPhotoLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<UnsplashPhotoLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = UnsplashPhotoLoadStateFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return LoadStateViewHolder(binding)
    }

    //loadState - show us if we are currently load new item or if there was an error
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        Log.d("proverka", if (loadState is LoadState.Loading) "loading" else "another")
        holder.bind(loadState)
    }

    //inner - cuz we want to have access from VH to properties of surrounded class (adapter)
    //another way we could put retry function in primary const. of LoadStateViewHolder
    inner class LoadStateViewHolder(private val binding: UnsplashPhotoLoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //invoke functions declared in constructor of adapter
        init {
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }
        //this adapter has only 1 or 0 items (header or footer)
            fun bind(loadState: LoadState) {
                binding.apply {
                    progressBar.isVisible = loadState is LoadState.Loading
                    //if itis not loading - means it is error
                    retryButton.isVisible = loadState !is LoadState.Loading
                    textViewError.isVisible = loadState !is LoadState.Loading

                }
            }

    }
}