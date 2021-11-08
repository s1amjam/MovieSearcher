package com.moviesearcher.movie.adapter.cast

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.databinding.MovieCastItemBinding
import com.moviesearcher.movie.model.cast.Cast
import com.moviesearcher.movie.model.cast.MovieCastResponse
import com.moviesearcher.utils.Constants

class MovieCastAdapter(
    private val castItems: MovieCastResponse,
) : RecyclerView.Adapter<MovieCastAdapter.MovieCastHolder>() {
    private lateinit var binding: MovieCastItemBinding

    inner class MovieCastHolder(binding: MovieCastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val name = binding.textViewName
        private val characterName = binding.textViewCharacterName
        private val poster: ImageView = binding.posterImageView

        fun bind(castItem: Cast) {
            name.text = castItem.name
            characterName.text = castItem.character

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + castItem.profile_path)
                .centerCrop()
                .override(400, 600)
                .into(poster)
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieCastHolder {
        binding = MovieCastItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieCastHolder(binding)
    }

    override fun getItemCount(): Int = castItems.cast?.size!!
    override fun onBindViewHolder(holder: MovieCastHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}