package com.moviesearcher.person.adapter.combinedcredits.images

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.databinding.ImagesItemBinding
import com.moviesearcher.person.model.images.PersonImagesResponse
import com.moviesearcher.person.model.images.Profile
import com.moviesearcher.utils.Constants

class PersonImagesAdapter(
    private val imagesItems: PersonImagesResponse,
) : RecyclerView.Adapter<PersonImagesAdapter.ImagesHolder>() {
    private lateinit var binding: ImagesItemBinding

    inner class ImagesHolder(binding: ImagesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val image = binding.imageImageView

        fun bind(imageItem: Profile) {
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