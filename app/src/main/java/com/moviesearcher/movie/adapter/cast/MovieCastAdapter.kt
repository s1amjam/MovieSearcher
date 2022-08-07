package com.moviesearcher.movie.adapter.cast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.MovieCastItemBinding
import com.moviesearcher.movie.MovieInfoFragmentDirections
import com.moviesearcher.movie.model.cast.Cast

class MovieCastAdapter(
    private val navController: NavController,
) : ListAdapter<Cast, MovieCastAdapter.MovieCastHolder>(
    AsyncDifferConfig.Builder(DiffCallback()).build()
) {

    inner class MovieCastHolder(private val binding: MovieCastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.castCardView.setOnClickListener {
                val castId = currentList[adapterPosition].id
                if (castId != null) {
                    navController.navigate(
                        MovieInfoFragmentDirections.actionMovieInfoFragmentToPersonInfoFragment(
                            castId
                        )
                    )
                }
            }
        }

        fun bind(castItem: Cast) {
            binding.apply {
                posterImageView.loadImage(
                    Constants.IMAGE_URL + castItem.profile_path,
                    isCardView = true
                )
                textViewName.text = castItem.name
                textViewCharacterName.text = castItem.character

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieCastAdapter.MovieCastHolder {
        return MovieCastHolder(
            MovieCastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieCastHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private class DiffCallback : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id;
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }
}