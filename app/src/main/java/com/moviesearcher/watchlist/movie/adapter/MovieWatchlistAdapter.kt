package com.moviesearcher.watchlist.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.MovieCardViewBinding
import com.moviesearcher.watchlist.WatchlistFragmentDirections
import com.moviesearcher.watchlist.common.model.WatchlistRequest
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse
import com.moviesearcher.watchlist.tv.model.MovieWatchlistResult

class MovieWatchlistAdapter(
    private val movieItems: MovieWatchlistResponse,
    private val navController: NavController,
    private val accountId: Long?,
    private val sessionId: String?,
    private val movieWatchlistIds: MutableList<Long>
) : RecyclerView.Adapter<MovieWatchlistAdapter.MovieHolder>() {
    private lateinit var binding: MovieCardViewBinding

    inner class MovieHolder(binding: MovieCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val imageViewWatchlist = binding.imageButtonWatchlist
        private val posterImageView = binding.posterImageView
        private val cardView = binding.trendingCardView

        fun bind(movieItem: MovieWatchlistResult) {
            val currentMovie = movieItem.id

            title.text = movieItem.title
            releaseDate.text = movieItem.releaseDate?.replace("-", ".")
            rating.text = movieItem.voteAverage.toString()

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + movieItem.posterPath)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(400, 600)
                .into(posterImageView)

            if (sessionId?.isNotBlank() == true || sessionId != null) {
                if (movieWatchlistIds.contains(currentMovie)) {
                    imageViewWatchlist.setImageResource(R.drawable.ic_baseline_bookmark_added_60)
                } else {
                    imageViewWatchlist.setImageResource(R.drawable.ic_baseline_bookmark_add_60)
                }
            }

            cardView.setOnClickListener {
                navController.navigate(
                    WatchlistFragmentDirections.actionWatchlistFragmentToMovieInfoFragment(
                        currentMovie!!
                    )
                )
            }

            imageViewWatchlist.setOnClickListener {
                if (sessionId?.isNotBlank() == true || sessionId != null) {
                    if (movieWatchlistIds.contains(currentMovie)) {
                        imageViewWatchlist
                            .setImageResource(R.drawable.ic_baseline_bookmark_add_60)

                        Api.watchlist(
                            accountId!!,
                            sessionId,
                            WatchlistRequest(false, currentMovie, "movie")
                        )
                        movieWatchlistIds.remove(currentMovie!!)
                    } else {
                        imageViewWatchlist
                            .setImageResource(R.drawable.ic_baseline_bookmark_added_60)

                        Api.watchlist(
                            accountId!!,
                            sessionId,
                            WatchlistRequest(true, currentMovie, "movie")
                        )
                        movieWatchlistIds.add(currentMovie!!)
                    }
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<MovieWatchlistResult>() {
        override fun areItemsTheSame(
            oldItem: MovieWatchlistResult,
            newItem: MovieWatchlistResult
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MovieWatchlistResult,
            newItem: MovieWatchlistResult
        ): Boolean {
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