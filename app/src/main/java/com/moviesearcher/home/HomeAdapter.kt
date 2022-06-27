package com.moviesearcher.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.common.toOneScale
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.MovieCardViewBinding
import com.moviesearcher.movie.model.Result
import com.moviesearcher.movie.model.TrendingResponse

class HomeAdapter(
    private val movieItems: TrendingResponse,
    private val navController: NavController,
) : RecyclerView.Adapter<HomeAdapter.MovieHolder>() {

    inner class MovieHolder(binding: MovieCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val posterImageView = binding.posterImageView
        private val cardView = binding.trendingCardView

        fun bind(movieItem: Result) {
            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + movieItem.posterPath)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(400, 600)
                .into(posterImageView)

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

            cardView.id = movieItem.id!!
            cardView.tag = movieItem.title
            rating.text = movieItem.voteAverage?.toOneScale()

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
        val binding = MovieCardViewBinding.inflate(
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