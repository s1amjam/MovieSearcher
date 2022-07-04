package com.moviesearcher.person.adapter.combinedcredits.images

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ImagesItemBinding
import com.moviesearcher.person.model.images.PersonImagesResponse
import com.moviesearcher.person.model.images.Profile

class PersonImagesAdapter(
    private val imagesItems: PersonImagesResponse,
) : RecyclerView.Adapter<PersonImagesAdapter.ImagesHolder>() {
    private lateinit var binding: ImagesItemBinding

    inner class ImagesHolder(binding: ImagesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val image = binding.imageImageView

        fun bind(imageItem: Profile) {
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

    private val differCallback = object : DiffUtil.ItemCallback<Profile>() {
        override fun areItemsTheSame(
            oldItem: Profile,
            newItem: Profile
        ): Boolean {
            return oldItem.filePath == newItem.filePath
        }

        override fun areContentsTheSame(
            oldItem: Profile,
            newItem: Profile
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int = imagesItems.profiles?.size!!
    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}