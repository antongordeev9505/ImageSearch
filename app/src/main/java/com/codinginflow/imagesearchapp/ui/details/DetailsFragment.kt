package com.codinginflow.imagesearchapp.ui.details

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.databinding.FragmentDetailsBinding

class DetailsFragment: Fragment(R.layout.fragment_details) {
    //we don`t need VM here, cus we have args
    //and we don`t need to hold up some states and we don`t have sny methods which call to the Repo

    //variable contains our nav args
    //DetailsFragmentArgs is generated
    private val args by navArgs<DetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //access our views in type safe way
        val binding = FragmentDetailsBinding.bind(view)
        binding.apply {
            val photo = args.photo

            //use fragment/activity as a context is more efficient than view`s context
            Glide.with(this@DetailsFragment)
                .load(photo.urls.full)
                .error(R.drawable.ic_error)
                    //listen the moment when image (Drawable) will be displayed to the screen
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //in this case placeholder will be displayed
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textViewCreator.isVisible = true
                        //cuz descriptions might be null (from Api)
                        textViewDescription.isVisible = photo.description != null
                        return false //otherwise we won`t see the image

                    }

                })
                .into(imageView)

            textViewDescription.text = photo.description

            //uri - address to the image
            val uri = Uri.parse(photo.user.attributionUrl)
            //ACTION_VIEW - let the system pick appropriate action to view this (uri) content
            //it`s implicit intent
            val intent = Intent(Intent.ACTION_VIEW, uri)
            val nameText = "Photo by ${photo.user.name} on Unsplash"

            textViewCreator.apply {
                text = nameText
                //make the text clickable
                setOnClickListener {
                    context.startActivity(intent)
                }
                //make the text is underlined
                paint.isUnderlineText = true
            }
        }
    }
}