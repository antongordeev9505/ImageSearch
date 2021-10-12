package com.codinginflow.imagesearchapp.ui.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.data.UnsplashCollection
import com.codinginflow.imagesearchapp.databinding.ItemUnsplashCollectionBinding


class UnsplashCollectionAdapter : PagingDataAdapter<UnsplashCollection, UnsplashCollectionAdapter.CollectionViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val binding = ItemUnsplashCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CollectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    //use viewBinding - let us to access our view in null type safeway
    class CollectionViewHolder(private val binding: ItemUnsplashCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) { //inflate item_unsplash_photo layout

        fun bind(collection: UnsplashCollection) {
            //bind views from itemview to data
            binding.apply {
                //need context of fragment here, instead of it we use itemView that is in Fragment
                Glide.with(itemView)
                    .load(collection.cover_photo.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

                textViewCollectionTitle.text = collection.title
                textViewTotalPhotos.text = collection.total_photos.toString()
            }
        }
    }

    companion object {
        //UnsplashPhoto - object we want to compare here
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashCollection>() {
            //compare 2 objects by id - true if the same
            override fun areItemsTheSame(oldItem: UnsplashCollection, newItem: UnsplashCollection) =
                oldItem.id == newItem.id

            //decide to refresh the layout of particular item
            //compare inner items
            //true if the contents of the items are the same
            //cuz we need UnsplashPhoto - data class cuz in data class equels method overrided
            override fun areContentsTheSame(oldItem: UnsplashCollection, newItem: UnsplashCollection) =
                oldItem == newItem
        }
    }
}