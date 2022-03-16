package com.moviesearcher.watchlist.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ExtendedCardViewBinding
import com.moviesearcher.watchlist.WatchlistFragmentDirections
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse
import com.moviesearcher.watchlist.tv.model.MovieWatchlistResult
import kotlin.collections.set

class MovieWatchlistAdapter(
    private val watchlistItems: MovieWatchlistResponse,
    private val navController: NavController,
    private val isTv: Boolean = false,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<MovieWatchlistAdapter.WatchlistViewHolder>() {

    inner class WatchlistViewHolder(val binding: ExtendedCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val poster = binding.posterImageView
        private val overview = binding.textViewDescription
        private val cardView = binding.cardView
        private val watchlistIb = binding.imageButtonRemove

        fun bind(movieItem: MovieWatchlistResult) {
            val mediaInfo: MutableMap<String, Long> = mutableMapOf()

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + movieItem.posterPath)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(400, 600)
                .into(poster)

            title.text = movieItem.title
            releaseDate.text = movieItem.releaseDate?.replace("-", ".")
            cardView.tag = movieItem.title
            overview.text = movieItem.overview
            rating.text = movieItem.getAverage()
            cardView.id = movieItem.id?.toInt()!!
            watchlistIb.setImageResource(R.drawable.ic_watchlist_added_36)
            watchlistIb.tag = "false"

            if (isTv) {
                mediaInfo["tv"] = cardView.id.toLong()

                cardView.setOnClickListener {
                    navController.navigate(
                        WatchlistFragmentDirections
                            .actionWatchlistFragmentToTvInfoFragment(movieItem.id)
                    )
                }
            } else {
                mediaInfo["movie"] = cardView.id.toLong()

                cardView.setOnClickListener {
                    navController.navigate(
                        WatchlistFragmentDirections
                            .actionWatchlistFragmentToMovieInfoFragment(movieItem.id)
                    )
                }
            }

            watchlistIb.setOnClickListener {
                itemClickListener.onItemClick(
                    watchlistIb,
                    mediaInfo,
                    binding.root.context,
                    binding.root.findViewTreeLifecycleOwner()!!
                )
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(button: ImageButton,
                        media: MutableMap<String, Long> = mutableMapOf(),
                        context: Context,
                        lifecycleOwner: LifecycleOwner
        )
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