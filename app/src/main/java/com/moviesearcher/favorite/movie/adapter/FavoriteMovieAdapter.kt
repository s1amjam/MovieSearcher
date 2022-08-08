package com.moviesearcher.favorite.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.R
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.extensions.toOneScale
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ExtendedCardViewBinding
import com.moviesearcher.favorite.FavoritesFragmentDirections
import com.moviesearcher.favorite.movie.model.ResultFavoriteMovie

class FavoriteMovieAdapter(
    private val navController: NavController,
    private val itemClickListener: ItemClickListener,
    private val isTv: Boolean = false,
) : ListAdapter<ResultFavoriteMovie, FavoriteMovieAdapter.FavoriteMovieViewHolder>(
    AsyncDifferConfig.Builder(DiffCallback()).build()
) {

    inner class FavoriteMovieViewHolder(
        private val binding: ExtendedCardViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var mediaId: Long = -1

        init {
            val mediaInfo: MutableMap<String, Long> = mutableMapOf()

            if (isTv) {
                mediaInfo["tv"] = mediaId

                binding.cardView.setOnClickListener {
                    navController.navigate(
                        FavoritesFragmentDirections
                            .actionFragmentFavoritesToTvInfoFragment(mediaId)
                    )
                }
            } else {
                mediaInfo["movie"] = mediaId

                binding.cardView.setOnClickListener {
                    navController.navigate(
                        FavoritesFragmentDirections
                            .actionFragmentFavoritesToMovieInfoFragment(mediaId)
                    )
                }
            }

            binding.imageButtonRemove.setOnClickListener {
                binding.root.findViewTreeLifecycleOwner()?.let { it1 ->
                    itemClickListener.onItemClick(
                        it as ImageButton,
                        mediaInfo,
                        binding.root.context,
                        it1
                    )
                }
            }
        }

        fun bind(movieItem: ResultFavoriteMovie) {
            binding.apply {
                if (movieItem.id != null) {
                    mediaId = movieItem.id
                }
                posterImageView.loadImage(
                    Constants.IMAGE_URL + movieItem.posterPath,
                    isCardView = true
                )
                textViewTitle.text = movieItem.title
                textViewReleaseDate.text = movieItem.releaseDate?.replace("-", ".")
                ratingImageView.setImageResource(R.drawable.ic_round_star_filled_36)
                textViewDescription.text = movieItem.overview
                textViewRating.text = movieItem.voteAverage?.toOneScale()
                imageButtonRemove.tag = "false"
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(
            button: ImageButton,
            media: MutableMap<String, Long> = mutableMapOf(),
            context: Context,
            lifecycleOwner: LifecycleOwner
        )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteMovieAdapter.FavoriteMovieViewHolder {
        return FavoriteMovieViewHolder(
            ExtendedCardViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private class DiffCallback : DiffUtil.ItemCallback<ResultFavoriteMovie>() {
        override fun areItemsTheSame(
            oldItem: ResultFavoriteMovie,
            newItem: ResultFavoriteMovie
        ): Boolean {
            return oldItem.id == newItem.id;
        }

        override fun areContentsTheSame(
            oldItem: ResultFavoriteMovie,
            newItem: ResultFavoriteMovie
        ): Boolean {
            return oldItem == newItem
        }
    }
}