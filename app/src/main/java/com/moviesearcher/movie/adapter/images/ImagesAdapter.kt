package com.moviesearcher.movie.adapter.images

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.model.images.Backdrop
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ImagesItemBinding

class ImagesAdapter : ListAdapter<Backdrop, ImagesAdapter.ImagesHolder>(
    AsyncDifferConfig.Builder(DiffCallback()).build()
) {

    inner class ImagesHolder(private val binding: ImagesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageItem: Backdrop) {
            binding.apply {
                imageImageView.loadImage(
                    Constants.IMAGE_URL + imageItem.filePath,
                    isCardView = true
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagesHolder {
        return ImagesHolder(
            ImagesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private class DiffCallback : DiffUtil.ItemCallback<Backdrop>() {
        override fun areItemsTheSame(oldItem: Backdrop, newItem: Backdrop): Boolean {
            return oldItem.filePath == newItem.filePath;
        }

        override fun areContentsTheSame(oldItem: Backdrop, newItem: Backdrop): Boolean {
            return oldItem == newItem
        }
    }
}