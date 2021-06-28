package com.moviesearcher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.FavoriteMoviesFragmentDirections
import com.moviesearcher.R
import com.moviesearcher.api.entity.favorites.FavoriteMovieResponse
import com.moviesearcher.api.entity.favorites.ResultFavoriteMovie
import com.moviesearcher.utils.Constants
import com.squareup.picasso.Picasso

class FavoriteMoviesAdapter(
    private val favoriteMovieItems: FavoriteMovieResponse,
    private val navController: NavController
) : RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMovieViewHolder>() {

    class FavoriteMovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val favoriteMovieItemPoster: ImageView =
            view.findViewById(R.id.image_view_favorite_movie_item)
        private val favoriteMovieItemName: TextView =
            view.findViewById(R.id.text_view_favorite_movie_item_name)
        private val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_favorite_movie_item)

        fun bind(favoriteMovieResultItem: ResultFavoriteMovie) {
            Picasso.get()
                .load(Constants.IMAGE_URL + favoriteMovieResultItem.posterPath)
                .into(favoriteMovieItemPoster)

            cardView.id = favoriteMovieResultItem.id?.toInt()!!
            favoriteMovieItemName.text = favoriteMovieResultItem.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteMovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_favorite_movie_item, parent, false)
        val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_favorite_movie_item)

        cardView.setOnClickListener {
            val movieId = it.id

            navController.navigate(
                FavoriteMoviesFragmentDirections.actionFragmentFavoriteMoviesToMovieInfoFragment(
                    movieId
                )
            )
        }

        return FavoriteMovieViewHolder(view)
    }

    override fun getItemCount(): Int = favoriteMovieItems.results?.size!!
    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        val favoriteMovieItem = favoriteMovieItems.results?.get(position)
        holder.bind(favoriteMovieItem!!)
    }
}