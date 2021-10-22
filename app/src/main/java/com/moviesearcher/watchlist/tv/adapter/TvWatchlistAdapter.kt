package com.moviesearcher.watchlist.tv.adapter

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
import com.moviesearcher.databinding.TrendingItemViewBinding
import com.moviesearcher.utils.Constants
import com.moviesearcher.watchlist.WatchlistFragmentDirections
import com.moviesearcher.watchlist.common.model.WatchlistRequest
import com.moviesearcher.watchlist.tv.model.TvWatchlistResponse
import com.moviesearcher.watchlist.tv.model.TvWatchlistResult

class TvWatchlistAdapter(
    private val tvItems: TvWatchlistResponse,
    private val navController: NavController,
    private val accountId: Long?,
    private val sessionId: String?,
    private val tvWatchlistIds: MutableList<Long>
) : ListAdapter<TvWatchlistResult, TvWatchlistAdapter.MovieHolder>(ITEM_COMPARATOR) {
    private lateinit var cardView: CardView
    private lateinit var posterImageView: ImageView
    private lateinit var binding: TrendingItemViewBinding

    inner class MovieHolder(binding: TrendingItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate

        fun bind(tvItem: TvWatchlistResult) {
            title.text = tvItem.name
            releaseDate.text = tvItem.firstAirDate?.replace("-", ".")
            rating.text = tvItem.voteAverage.toString()

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + tvItem.posterPath)
                .centerCrop()
                .override(400, 600)
                .into(posterImageView)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieHolder {
        binding = TrendingItemViewBinding.inflate(
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

    override fun getItemCount(): Int = tvItems.results?.size!!
    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val imageButtonWatchlist = binding.imageButtonWatchlist
        val currentTv = tvItems.results?.get(position)?.id
        posterImageView = binding.posterImageView
        cardView = binding.trendingCardView

        if (sessionId?.isNotBlank() == true || sessionId != null) {
            if (tvWatchlistIds.contains(currentTv)) {
                imageButtonWatchlist.setImageResource(R.drawable.ic_baseline_bookmark_added_60)
            } else {
                imageButtonWatchlist.setImageResource(R.drawable.ic_baseline_bookmark_add_60)
            }
        }

        cardView.setOnClickListener {
            navController.navigate(
                WatchlistFragmentDirections.actionWatchlistFragmentToTvInfoFragment(currentTv!!)
            )
        }

        imageButtonWatchlist.setOnClickListener {
            if (sessionId?.isNotBlank() == true || sessionId != null) {
                if (tvWatchlistIds.contains(currentTv)) {
                    Api.watchlist(
                        accountId!!,
                        sessionId,
                        WatchlistRequest(false, currentTv, "tv")
                    )
                    imageButtonWatchlist
                        .setImageResource(R.drawable.ic_baseline_bookmark_add_60)
                    tvWatchlistIds.remove(currentTv!!)
                } else {
                    Api.watchlist(
                        accountId!!,
                        sessionId,
                        WatchlistRequest(true, currentTv, "tv")
                    )
                    imageButtonWatchlist
                        .setImageResource(R.drawable.ic_baseline_bookmark_added_60)
                    tvWatchlistIds.add(currentTv!!)
                }
            }
        }

        val tvItem = tvItems.results?.get(position)
        holder.bind(tvItem!!)
    }
}

private val ITEM_COMPARATOR = object :
    DiffUtil.ItemCallback<TvWatchlistResult>() {
    override fun areItemsTheSame(
        oldItem: TvWatchlistResult,
        newItem: TvWatchlistResult
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TvWatchlistResult,
        newItem: TvWatchlistResult
    ): Boolean {
        return oldItem == newItem
    }
}