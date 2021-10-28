package com.moviesearcher.movie.adapter.images

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.common.model.images.Backdrop
import com.moviesearcher.common.model.images.ImagesResponse
import com.moviesearcher.databinding.ImagesItemBinding
import com.moviesearcher.utils.Constants

class ImagesAdapter(
    private val imagesItems: ImagesResponse,
) : RecyclerView.Adapter<ImagesAdapter.ImagesHolder>() {
    private lateinit var binding: ImagesItemBinding

    inner class ImagesHolder(binding: ImagesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val image = binding.imageImageView

        fun bind(imageItem: Backdrop) {
            val imageResp = imageItem.filePath

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + imageResp)
                .centerCrop()
                .override(400, 600)
                .into(image)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagesHolder {
        binding = ImagesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ImagesHolder(binding)
    }

    override fun getItemCount(): Int = imagesItems.backdrops?.size!!
    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {
        val imageItem = imagesItems.backdrops?.get(position)
        holder.bind(imageItem!!)
    }
}