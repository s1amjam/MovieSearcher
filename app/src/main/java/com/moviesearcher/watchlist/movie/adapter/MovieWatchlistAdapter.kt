package com.moviesearcher.watchlist.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.common.utils.OnClickListener
import com.moviesearcher.databinding.ExtendedCardViewBinding
import com.moviesearcher.watchlist.WatchlistFragmentDirections
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse
import com.moviesearcher.watchlist.tv.model.MovieWatchlistResult

class MovieWatchlistAdapter(
    private val watchlistItems: MovieWatchlistResponse,
    private val navController: NavController,
    private val onClickListener: OnClickListener? = null
) : RecyclerView.Adapter<MovieWatchlistAdapter.WatchlistViewHolder>() {

    inner class WatchlistViewHolder(val binding: ExtendedCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val mediaInfo: MutableMap<String, Long> = mutableMapOf()

        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val poster = binding.posterImageView
        private val overview = binding.textViewDescription
        private val cardView = binding.cardView
        private val watchlistIb = binding.imageButtonRemove

        fun bind(movieItem: MovieWatchlistResult) {
            title.text = movieItem.title
            releaseDate.text = movieItem.releaseDate?.replace("-", ".")

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + movieItem.posterPath)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(400, 600)
                .into(poster)

            cardView.tag = movieItem.title
            overview.text = movieItem.overview
            rating.text = movieItem.getAverage()
            cardView.id = movieItem.id?.toInt()!!
            watchlistIb.setImageResource(R.drawable.ic_baseline_bookmark_added_60)
            watchlistIb.tag = "false"

            mediaInfo["movie"] = cardView.id.toLong()
            watchlistIb.setOnClickListener {
                onClickListener?.onClick(watchlistIb, mediaInfo)
            }

            cardView.setOnClickListener {
                navController.navigate(
                    WatchlistFragmentDirections
                        .actionWatchlistFragmentToMovieInfoFragment(movieItem.id)
                )
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
    ): MovieWatchlistAdapter.WatchlistViewHolder {
        val binding = ExtendedCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return WatchlistViewHolder(binding)
    }

    override fun getItemCount(): Int = watchlistItems.results?.size!!
    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}