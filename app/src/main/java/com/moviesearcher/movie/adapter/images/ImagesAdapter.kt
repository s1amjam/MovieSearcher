package com.moviesearcher.movie.adapter.images

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.common.model.images.Backdrop
import com.moviesearcher.common.model.images.ImagesResponse
import com.moviesearcher.databinding.ImagesItemBinding
import com.moviesearcher.utils.Constants

class ImagesAdapter(
    private val imagesItems: ImagesResponse,
) : RecyclerView.Adapter<ImagesAdapter.ImagesHolder>() {
    private lateinit var binding: ImagesItemBinding

    inner class ImagesHolder(binding: ImagesItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val image = binding.imageImageView

        fun bind(imageItem: Backdrop) {
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

    private val differCallback = object : DiffUtil.ItemCallback<Backdrop>() {
        override fun areItemsTheSame(
            oldItem: Backdrop,
            newItem: Backdrop
        ): Boolean {
            return oldItem.filePath == newItem.filePath
        }

        override fun areContentsTheSame(
            oldItem: Backdrop,
            newItem: Backdrop
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int = imagesItems.backdrops?.size!!
    override fun onBindViewHolder(holder: ImagesHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}