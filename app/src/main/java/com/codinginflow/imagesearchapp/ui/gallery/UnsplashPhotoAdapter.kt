package com.codinginflow.imagesearchapp.ui.gallery

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.data.UnsplashPhoto
import com.codinginflow.imagesearchapp.databinding.ItemUnsplashPhotoBinding

//pagingadapter knows how to handle paging data - UnsplashPhoto
//in constructor we put Diffutill Item callback - know how to calculate changes between old and new datasets
//pass the fragment as the listener to the adapter
class UnsplashPhotoAdapter(private val listener: OnItemClickListener) : PagingDataAdapter<UnsplashPhoto, UnsplashPhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemUnsplashPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    //use viewBinding - let us to access our view in null type safeway
    inner class PhotoViewHolder(private val binding: ItemUnsplashPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) { //inflate item_unsplash_photo layout

        init {
            //listener
            //binding.root - whole item
            //we have to create interface to send click to the fragment cuz we must handle click in Fragment that contains RV
            //also we can`t handle navigation from the adapter
            binding.root.setOnClickListener {
                //adapter position of the item
                val position = bindingAdapterPosition
                //NO_POSITION - if the item is animated during our click
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)

                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

            fun bind(photo: UnsplashPhoto) {
                //bind views from itemview to data
                binding.apply {
                    //need context of fragment here, instead of it we use itemView that is in Fragment
                    Glide.with(itemView)
                        .load(photo.urls.regular)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageView)

                    textViewUserName.text = photo.user.username
                }
            }
    }

    //create interface - advantage: keep adaptor reusable
    interface OnItemClickListener {
        fun onItemClick(photo: UnsplashPhoto)
    }

    companion object {
        //UnsplashPhoto - object we want to compare here
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            //compare 2 objects by id - true if the same
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem.id == newItem.id

            //decide to refresh the layout of particular item
            //compare inner items
            //true if the contents of the items are the same
            //cuz we need UnsplashPhoto - data class cuz in data class equels method overrided
            override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem == newItem
        }
    }


}