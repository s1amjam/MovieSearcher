package com.moviesearcher.person.adapter.combinedcredits.images

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ImagesItemBinding
import com.moviesearcher.person.model.images.Profile

class PersonImagesAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<Profile, PersonImagesAdapter.ImagesHolder>(
        AsyncDifferConfig.Builder(DiffCallback()).build()
    ) {

    inner class ImagesHolder(private val binding: ImagesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imagesCardView.setOnClickListener {
                onClick(currentList[adapterPosition].filePath.toString())
            }
        }

        fun bind(imageItem: Profile) {
            binding.imageImageView.loadImage(
                Constants.IMAGE_URL + imageItem.filePath,
                isCardView = true
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesHolder {
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

    private class DiffCallback : DiffUtil.ItemCallback<Profile>() {
        override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            return oldItem.filePath == newItem.filePath;
        }

        override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
            return oldItem == newItem
        }
    }
}