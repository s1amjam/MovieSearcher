package com.moviesearcher.watchlist.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.databinding.MovieCardViewBinding
import com.moviesearcher.utils.Constants
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
) : ListAdapter<MovieWatchlistResult, MovieWatchlistAdapter.MovieHolder>(ITEM_COMPARATOR) {
    private lateinit var cardView: CardView
    private lateinit var posterImageView: ImageView
    private lateinit var binding: MovieCardViewBinding

    inner class MovieHolder(binding: MovieCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate

        fun bind(movieItem: MovieWatchlistResult) {
            title.text = movieItem.title
            releaseDate.text = movieItem.releaseDate?.replace("-", ".")
            rating.text = movieItem.voteAverage.toString()

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + movieItem.posterPath)
                .centerCrop()
                .override(400, 600)
                .into(posterImageView)
        }
    }

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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = movieItems.results?.size!!
    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val imageButtonWatchlist = binding.imageButtonWatchlist
        val currentMovie = movieItems.results?.get(position)?.id
        posterImageView = binding.posterImageView
        cardView = binding.trendingCardView

        if (sessionId?.isNotBlank() == true || sessionId != null) {
            if (movieWatchlistIds.contains(currentMovie)) {
                imageButtonWatchlist.setImageResource(R.drawable.ic_baseline_bookmark_added_60)
            } else {
                imageButtonWatchlist.setImageResource(R.drawable.ic_baseline_bookmark_add_60)
            }
        }

        cardView.setOnClickListener {
            navController.navigate(
                WatchlistFragmentDirections.actionWatchlistFragmentToMovieInfoFragment(currentMovie!!)
            )
        }

        imageButtonWatchlist.setOnClickListener {
            if (sessionId?.isNotBlank() == true || sessionId != null) {
                if (movieWatchlistIds.contains(currentMovie)) {
                    imageButtonWatchlist
                        .setImageResource(R.drawable.ic_baseline_bookmark_add_60)

                    Api.watchlist(
                        accountId!!,
                        sessionId,
                        WatchlistRequest(false, currentMovie, "movie")
                    )
                    movieWatchlistIds.remove(currentMovie!!)
                } else {
                    imageButtonWatchlist
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

        val movieItem = movieItems.results?.get(position)
        holder.bind(movieItem!!)
    }
}

private val ITEM_COMPARATOR = object :
    DiffUtil.ItemCallback<MovieWatchlistResult>() {
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