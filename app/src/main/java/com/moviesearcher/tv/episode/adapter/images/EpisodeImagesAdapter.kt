package com.moviesearcher.tv.episode.adapter.images

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ImagesItemBinding
import com.moviesearcher.tv.episode.model.image.EpisodeImageResponse
import com.moviesearcher.tv.episode.model.image.Still

class EpisodeImagesAdapter(
    private val imagesItems: EpisodeImageResponse,
) : RecyclerView.Adapter<EpisodeImagesAdapter.ImagesHolder>() {
    private lateinit var binding: ImagesItemBinding

    inner class ImagesHolder(binding: ImagesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image = binding.imageImageView

        fun bind(imageItem: Still) {
            val imageResp = imageItem.filePath

            image.loadImage(Constants.IMAGE_URL + imageResp, isCardView = true)
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