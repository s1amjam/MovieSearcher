package com.moviesearcher.movie.adapter

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
import com.moviesearcher.databinding.MovieCardViewBinding
import com.moviesearcher.home.HomeFragmentDirections
import com.moviesearcher.movie.model.Result
import com.moviesearcher.movie.model.TrendingResponse

class TrendingAdapter(
    private val movieItems: TrendingResponse,
    private val navController: NavController,
    private val sessionId: String?,
    private val movieWatchlistIds: MutableList<Long>?,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<TrendingAdapter.MovieHolder>() {
    private lateinit var binding: MovieCardViewBinding

    inner class MovieHolder(binding: MovieCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val imageButtonWatchlist = binding.imageButtonWatchlist
        private val posterImageView = binding.posterImageView
        private val cardView = binding.trendingCardView

        private val mediaInfo: MutableMap<String, Long> = mutableMapOf()

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
            rating.text = movieItem.getAverage()

            if (sessionId?.isNotBlank() == true || sessionId != null) {
                if (movieWatchlistIds?.contains(movieItems.results?.get(position)?.id?.toLong()) == true) {
                    imageButtonWatchlist.setImageResource(R.drawable.ic_baseline_bookmark_added_60)
                    imageButtonWatchlist.tag = "false"
                } else {
                    imageButtonWatchlist.setImageResource(R.drawable.ic_baseline_bookmark_add_60)
                    imageButtonWatchlist.tag = "true"
                }
            }

            cardView.setOnClickListener {
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

            if (cardView.tag != null) {
                mediaInfo["movie"] = cardView.id.toLong()
                imageButtonWatchlist.setOnClickListener {
                    onClickListener.onClick(imageButtonWatchlist, mediaInfo)
                }
            } else {
                mediaInfo["tv"] = cardView.id.toLong()
                imageButtonWatchlist.setOnClickListener {
                    onClickListener.onClick(imageButtonWatchlist, mediaInfo)
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