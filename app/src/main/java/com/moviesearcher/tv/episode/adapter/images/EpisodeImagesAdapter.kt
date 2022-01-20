package com.moviesearcher.tv.episode.adapter.images

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.databinding.ImagesItemBinding
import com.moviesearcher.tv.episode.model.image.EpisodeImageResponse
import com.moviesearcher.tv.episode.model.image.Still
import com.moviesearcher.common.utils.Constants

class EpisodeImagesAdapter(
    private val imagesItems: EpisodeImageResponse,
) : RecyclerView.Adapter<EpisodeImagesAdapter.ImagesHolder>() {
    private lateinit var binding: ImagesItemBinding

    inner class ImagesHolder(binding: ImagesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image = binding.imageImageView

        fun bind(imageItem: Still) {
            val imageResp = imageItem.filePath

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + imageResp)
                .placeholder(R.drawable.ic_placeholder)
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

    private val differCallback = object : DiffUtil.ItemCallback<Still>() {
        override fun areItemsTheSame(
            oldItem: Still,
            newItem: Still
        ): Boolean {
            return oldItem.filePath == newItem.filePath
        }

        override fun areContentsTheSame(
            oldItem: Still,
            newItem: Still
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int = imagesItems.stills?.size!!
    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}