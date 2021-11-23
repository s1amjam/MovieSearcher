package com.moviesearcher.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.HomeFragmentDirections
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.databinding.MovieCardViewBinding
import com.moviesearcher.movie.model.Result
import com.moviesearcher.movie.model.TrendingResponse
import com.moviesearcher.utils.Constants
import com.moviesearcher.watchlist.common.model.WatchlistRequest

class TrendingAdapter(
    private val movieItems: TrendingResponse,
    private val navController: NavController,
    private val accountId: Long?,
    private val sessionId: String?,
    private val movieWatchlistIds: MutableList<Long>
) : RecyclerView.Adapter<TrendingAdapter.MovieHolder>() {
    private lateinit var binding: MovieCardViewBinding

    inner class MovieHolder(binding: MovieCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val imageViewWatchlist = binding.imageViewWatchlist
        private val posterImageView = binding.posterImageView
        private val cardView = binding.trendingCardView

        fun bind(movieItem: Result) {
            if (movieItem.title != null) {
                title.text = movieItem.title
            } else if (movieItem.name != null) {
                title.text = movieItem.name
            }

            if (movieItem.releaseDate != null) {
                releaseDate.text = movieItem.releaseDate.replace("-", ".")
            } else if (movieItem.firstAirDate != null) {
                releaseDate.text = movieItem.firstAirDate.replace("-", ".")
            }

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + movieItem.posterPath)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(400, 600)
                .into(posterImageView)

            cardView.id = movieItem.id!!
            cardView.tag = movieItem.title
            rating.text = movieItem.voteAverage.toString()

            if (sessionId?.isNotBlank() == true || sessionId != null) {
                if (movieWatchlistIds.contains(movieItems.results?.get(position)?.id?.toLong())) {
                    imageViewWatchlist.setImageResource(R.drawable.ic_baseline_bookmark_added_60)
                } else {
                    imageViewWatchlist.setImageResource(R.drawable.ic_baseline_bookmark_add_60)
                }
            }

            cardView.setOnClickListener {
                val movieId = it.id.toLong()

                //Only 'Movie' has a 'title', 'Tv series' has a 'name', so binding title to tag
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

            imageViewWatchlist.setOnClickListener {
                val movieItemId = movieItems.results?.get(position)?.id?.toLong()

                if (sessionId?.isNotBlank() == true || sessionId != null) {
                    if (cardView.tag != null) {
                        if (movieWatchlistIds.contains(movieItemId)) {
                            imageViewWatchlist
                                .setImageResource(R.drawable.ic_baseline_bookmark_add_60)

                            Api.watchlist(
                                accountId!!,
                                sessionId,
                                WatchlistRequest(false, movieItemId, "movie")
                            )
                            movieWatchlistIds.remove(movieItemId!!)
                        } else {
                            imageViewWatchlist
                                .setImageResource(R.drawable.ic_baseline_bookmark_added_60)

                            Api.watchlist(
                                accountId!!,
                                sessionId,
                                WatchlistRequest(true, movieItemId, "movie")
                            )
                            movieWatchlistIds.add(movieItemId!!)
                        }
                    } else {
                        if (movieWatchlistIds.contains(movieItemId)) {
                            Api.watchlist(
                                accountId!!,
                                sessionId,
                                WatchlistRequest(false, movieItemId, "tv")
                            )
                            imageViewWatchlist
                                .setImageResource(R.drawable.ic_baseline_bookmark_add_60)
                            movieWatchlistIds.remove(movieItemId!!)
                        } else {
                            Api.watchlist(
                                accountId!!,
                                sessionId,
                                WatchlistRequest(true, movieItemId, "tv")
                            )
                            imageViewWatchlist
                                .setImageResource(R.drawable.ic_baseline_bookmark_added_60)
                            movieWatchlistIds.add(movieItemId!!)
                        }
                    }
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieHolder {
        binding = MovieCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieHolder(binding)
    }

    override fun getItemCount(): Int = movieItems.results?.size!!
    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}