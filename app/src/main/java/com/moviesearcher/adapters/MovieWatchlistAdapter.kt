package com.moviesearcher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.R
import com.moviesearcher.WatchlistFragmentDirections
import com.moviesearcher.api.entity.watchlist.movie.MovieWatchlistResponse
import com.moviesearcher.api.entity.watchlist.movie.MovieWatchlistResult
import com.moviesearcher.utils.Constants
import com.squareup.picasso.Picasso

class MovieWatchlistAdapter(
    private val movieWatchlistItems: MovieWatchlistResponse,
    private val navController: NavController
) : RecyclerView.Adapter<MovieWatchlistAdapter.MovieWatchlistViewHolder>() {

    class MovieWatchlistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val movieWatchlistItemPoster: ImageView =
            view.findViewById(R.id.image_view_movie_watchlist_item)
        private val movieWatchlistItemName: TextView =
            view.findViewById(R.id.text_view_movie_watchlist_item_name)
        private val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_movie_watchlist_item)

        fun bind(movieWatchlistResultItem: MovieWatchlistResult) {
            Picasso.get()
                .load(Constants.IMAGE_URL + movieWatchlistResultItem.posterPath)
                .into(movieWatchlistItemPoster)

            cardView.id = movieWatchlistResultItem.id?.toInt()!!
            movieWatchlistItemName.text = movieWatchlistResultItem.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieWatchlistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movie_watchlist_item, parent, false)
        val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_movie_watchlist_item)

        cardView.setOnClickListener {
            val movieId = it.id.toLong()

            navController.navigate(
                WatchlistFragmentDirections.actionWatchlistFragmentToMovieInfoFragment(
                    movieId
                )
            )
        }

        return MovieWatchlistViewHolder(view)
    }

    override fun getItemCount(): Int = movieWatchlistItems.results?.size!!
    override fun onBindViewHolder(holder: MovieWatchlistViewHolder, position: Int) {
        val movieWatchlistItem = movieWatchlistItems.results?.get(position)
        holder.bind(movieWatchlistItem!!)
    }
}