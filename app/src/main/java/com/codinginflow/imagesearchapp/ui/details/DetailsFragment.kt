package com.codinginflow.imagesearchapp.ui.details

import android.app.DownloadManager
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.work.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.codinginflow.imagesearchapp.DownloadImageWorker
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
                        fab.isVisible = true
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

            fab.setOnClickListener {
                downloadImage(photo.urls.full)
            }
        }
    }

    private fun downloadImage(imageUrl: String) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .setRequiredNetworkType(NetworkType.NOT_ROAMING)
            .build()

        val downloadImageWorker = OneTimeWorkRequestBuilder<DownloadImageWorker>()
            //image - inputdata in worker, we will use it in Worker
            .setInputData(workDataOf("image_path" to imageUrl))
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(requireContext())
        workManager.enqueue(downloadImageWorker)

        workManager.getWorkInfoByIdLiveData(downloadImageWorker.id).observe(viewLifecycleOwner, { info->
            if (info.state.isFinished) {
                Toast.makeText(context, "Downloaded", Toast.LENGTH_SHORT).show()
            }
        })
    }
}