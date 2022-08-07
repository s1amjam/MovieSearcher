package com.moviesearcher.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.extensions.toOneScale
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.MovieCardViewBinding
import com.moviesearcher.movie.model.Result

class HomeAdapter(
    private val navController: NavController,
) : ListAdapter<Result, HomeAdapter.MovieHolder>(
    AsyncDifferConfig.Builder(DiffCallback()).build()
) {

    inner class MovieHolder(private val binding: MovieCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.trendingCardView.setOnClickListener {
                val movieId = it.id.toLong()

                /**
                 * Only 'Movie' has a 'title', 'Tv series' has a 'name', so binding 'title'(movie)
                 * to 'tag'
                 */
                if (it.tag != null) {
                    navController.navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToMovieInfoFragment(movieId)
                    )
                } else {
                    navController.navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToTvInfoFragment(movieId)
                    )
                }
            }
        }

        fun bind(movieItem: Result) {
            binding.apply {
                posterImageView.loadImage(
                    Constants.IMAGE_URL + movieItem.posterPath,
                    isCardView = true
                )

                if (movieItem.title != null) {
                    textViewTitle.text = movieItem.title
                } else if (movieItem.name != null) {
                    textViewTitle.text = movieItem.name
                }

                if (movieItem.releaseDate != null) {
                    textViewReleaseDate.text = movieItem.releaseDate.replace("-", ".")
                } else if (movieItem.firstAirDate != null) {
                    textViewReleaseDate.text = movieItem.firstAirDate.replace("-", ".")
                }

                movieItem.id.let {
                    if (it != null) {
                        trendingCardView.id = it
                    }
                }
                trendingCardView.tag = movieItem.title
                textViewRating.text = movieItem.voteAverage?.toOneScale()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(
            MovieCardViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private class DiffCallback : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id;
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
}