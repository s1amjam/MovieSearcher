package com.moviesearcher.watchlist.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.databinding.FragmentMovieWatchlistItemBinding
import com.moviesearcher.utils.Constants
import com.moviesearcher.watchlist.WatchlistFragmentDirections
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse
import com.moviesearcher.watchlist.tv.model.MovieWatchlistResult

class MovieWatchlistAdapter(
    private val movieWatchlistItems: MovieWatchlistResponse,
    private val navController: NavController
) : RecyclerView.Adapter<MovieWatchlistAdapter.MovieWatchlistViewHolder>() {
    lateinit var cardView: MaterialCardView

    inner class MovieWatchlistViewHolder(binding: FragmentMovieWatchlistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val movieWatchlistItemPoster: ImageView = binding.imageViewMovieWatchlistItem
        private val movieWatchlistItemName: TextView = binding.textViewMovieWatchlistItemName

        fun bind(movieWatchlistResultItem: MovieWatchlistResult) {
            Glide.with(this.itemView)
                .load(Constants.IMAGE_URL + movieWatchlistResultItem.posterPath)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(movieWatchlistItemPoster)
            cardView.id = movieWatchlistResultItem.id?.toInt()!!
            movieWatchlistItemName.text = movieWatchlistResultItem.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieWatchlistViewHolder {
        val binding = FragmentMovieWatchlistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        cardView = binding.materialCardViewMovieWatchlistItem

        cardView.setOnClickListener {
            val movieId = it.id.toLong()

            navController.navigate(
                WatchlistFragmentDirections.actionWatchlistFragmentToMovieInfoFragment(
                    movieId
                )
            )
        }

        return MovieWatchlistViewHolder(binding)
    }

    override fun getItemCount(): Int = movieWatchlistItems.results?.size!!
    override fun onBindViewHolder(holder: MovieWatchlistViewHolder, position: Int) {
        val movieWatchlistItem = movieWatchlistItems.results?.get(position)
        holder.bind(movieWatchlistItem!!)
    }
}