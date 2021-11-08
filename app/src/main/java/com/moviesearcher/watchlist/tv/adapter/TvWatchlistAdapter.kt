package com.moviesearcher.watchlist.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.databinding.MovieCardViewBinding
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
) : RecyclerView.Adapter<TvWatchlistAdapter.MovieHolder>() {
    private lateinit var binding: MovieCardViewBinding

    inner class MovieHolder(binding: MovieCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val imageViewWatchlist = binding.imageViewWatchlist
        private val posterImageView = binding.posterImageView
        private val cardView = binding.trendingCardView

        fun bind(tvItem: TvWatchlistResult) {
            val currentTv = tvItem.id

            title.text = tvItem.name
            releaseDate.text = tvItem.firstAirDate?.replace("-", ".")
            rating.text = tvItem.voteAverage.toString()

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + tvItem.posterPath)
                .centerCrop()
                .override(400, 600)
                .into(posterImageView)

            if (sessionId?.isNotBlank() == true || sessionId != null) {
                if (tvWatchlistIds.contains(currentTv)) {
                    imageViewWatchlist.setImageResource(R.drawable.ic_baseline_bookmark_added_60)
                } else {
                    imageViewWatchlist.setImageResource(R.drawable.ic_baseline_bookmark_add_60)
                }
            }

            cardView.setOnClickListener {
                navController.navigate(
                    WatchlistFragmentDirections.actionWatchlistFragmentToTvInfoFragment(currentTv!!)
                )
            }

            imageViewWatchlist.setOnClickListener {
                if (sessionId?.isNotBlank() == true || sessionId != null) {
                    if (tvWatchlistIds.contains(currentTv)) {
                        Api.watchlist(
                            accountId!!,
                            sessionId,
                            WatchlistRequest(false, currentTv, "tv")
                        )
                        imageViewWatchlist
                            .setImageResource(R.drawable.ic_baseline_bookmark_add_60)
                        tvWatchlistIds.remove(currentTv!!)
                    } else {
                        Api.watchlist(
                            accountId!!,
                            sessionId,
                            WatchlistRequest(true, currentTv, "tv")
                        )
                        imageViewWatchlist
                            .setImageResource(R.drawable.ic_baseline_bookmark_added_60)
                        tvWatchlistIds.add(currentTv!!)
                    }
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<TvWatchlistResult>() {
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

    override fun getItemCount(): Int = tvItems.results?.size!!
    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}