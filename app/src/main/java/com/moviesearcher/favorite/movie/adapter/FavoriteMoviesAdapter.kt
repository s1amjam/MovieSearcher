package com.moviesearcher.favorite.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.databinding.FragmentFavoriteMovieItemBinding
import com.moviesearcher.favorite.FavoritesFragmentDirections
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.movie.model.ResultFavoriteMovie
import com.moviesearcher.utils.Constants

class FavoriteMoviesAdapter(
    private val favoriteMovieItems: FavoriteMovieResponse,
    private val navController: NavController
) : RecyclerView.Adapter<FavoriteMoviesAdapter.FavoriteMovieViewHolder>() {
    lateinit var cardView: MaterialCardView

    inner class FavoriteMovieViewHolder(binding: FragmentFavoriteMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val favoriteMovieItemPoster: ImageView = binding.imageViewFavoriteMovieItem
        private val favoriteMovieItemName: TextView = binding.textViewFavoriteMovieItemName

        fun bind(favoriteMovieResultItem: ResultFavoriteMovie) {
            Glide.with(this.itemView)
                .load(Constants.IMAGE_URL + favoriteMovieResultItem.posterPath)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(favoriteMovieItemPoster)
            cardView.id = favoriteMovieResultItem.id?.toInt()!!
            favoriteMovieItemName.text = favoriteMovieResultItem.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteMovieViewHolder {
        val binding = FragmentFavoriteMovieItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        cardView = binding.materialCardViewFavoriteMovieItem

        cardView.setOnClickListener {
            val movieId = it.id

            navController.navigate(
                FavoritesFragmentDirections.actionFragmentFavoritesToMovieInfoFragment(
                    movieId.toLong()
                )
            )
        }

        return FavoriteMovieViewHolder(binding)
    }

    override fun getItemCount(): Int = favoriteMovieItems.results?.size!!
    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        val favoriteMovieItem = favoriteMovieItems.results?.get(position)
        holder.bind(favoriteMovieItem!!)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}