package com.moviesearcher.movie.adapter.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.common.model.videos.Result
import com.moviesearcher.common.model.videos.VideosResponse
import com.moviesearcher.databinding.VideoItemViewBinding
import com.moviesearcher.movie.MovieInfoFragmentDirections
import com.moviesearcher.common.utils.Constants

class VideoAdapter(
    private val videoItems: VideosResponse,
    private val navController: NavController,
) : RecyclerView.Adapter<VideoAdapter.VideoHolder>() {
    private lateinit var binding: VideoItemViewBinding

    inner class VideoHolder(binding: VideoItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val preview = binding.previewImageView
        private val cardView = binding.videoCardView
        private val videoName = binding.videoNameTextView

        fun bind(videoItem: Result) {
            val videoKey = videoItem.key

            Glide.with(this.itemView.context)
                .load(Constants.YOUTUBE_PREVIEW_URL.format(videoItem.key))
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(480, 360)
                .into(preview)

            videoName.text = videoItem.name

            cardView.setOnClickListener {
                navController.navigate(
                    MovieInfoFragmentDirections.actionMovieInfoFragmentToVideoFragment(videoKey!!)
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoAdapter.VideoHolder {
        binding = VideoItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return VideoHolder(binding)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(
            oldItem: Result,
            newItem: Result
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Result,
            newItem: Result
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int = videoItems.results?.size!!
    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}