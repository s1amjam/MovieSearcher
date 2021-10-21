package com.moviesearcher.favorite.movie.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.moviesearcher.api.Api
import com.moviesearcher.databinding.FragmentFavoriteMovieItemBinding
import com.moviesearcher.favorite.FavoritesFragmentDirections
import com.moviesearcher.favorite.common.model.MarkAsFavoriteRequest
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.movie.model.ResultFavoriteMovie
import com.moviesearcher.utils.Constants
import com.moviesearcher.utils.Constants.DURATION_5_SECONDS

class FavoriteMovieAdapter(
    private val favoriteMovieItems: FavoriteMovieResponse,
    private val navController: NavController,
    private val sessionId: String,
    private val accountId: Long,
) : RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {
    private lateinit var cardView: MaterialCardView
    private lateinit var imageButtonRemoveFromFavorite: ImageButton

    inner class FavoriteMovieViewHolder(val binding: FragmentFavoriteMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val poster = binding.posterImageView
        private val overview = binding.textViewDescription

        fun bind(movieItem: ResultFavoriteMovie) {
            title.text = movieItem.title
            releaseDate.text = movieItem.releaseDate?.replace("-", ".")

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + movieItem.posterPath)
                .centerCrop()
                .override(400, 600)
                .into(poster)

            overview.text = movieItem.overview
            rating.text = movieItem.voteAverage.toString()
            cardView.id = movieItem.id?.toInt()!!
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
        cardView = binding.favoriteCardView
        imageButtonRemoveFromFavorite = binding.imageButtonRemoveFromFavorite

        return FavoriteMovieViewHolder(binding)
    }

    override fun getItemCount(): Int = favoriteMovieItems.results?.size!!

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        imageButtonRemoveFromFavorite.setOnClickListener {
            val movieToRemove = favoriteMovieItems.results?.get(position)

            val removeFromFavoriteResponse =
                Api.markAsFavorite(
                    accountId,
                    sessionId,
                    MarkAsFavoriteRequest(
                        false,
                        movieToRemove?.id!!.toLong(),
                        "movie"
                    ),
                )

            removeFromFavoriteResponse.observe(holder.binding.root.findViewTreeLifecycleOwner()!!, {
                if (it.success) {
                    favoriteMovieItems.results?.removeAt(position)
                    notifyItemRemoved(position)

                    val favoriteItemRemovedSnackbar = Snackbar.make(
                        holder.binding.root.rootView,
                        "${movieToRemove.title} was removed from Favorites",
                        DURATION_5_SECONDS
                    )

                    favoriteItemRemovedSnackbar.setAction("UNDO") { view ->
                        val addToFavoriteResponse =
                            Api.markAsFavorite(
                                accountId,
                                sessionId,
                                MarkAsFavoriteRequest(
                                    true,
                                    movieToRemove.id.toLong(),
                                    "movie"
                                ),
                            )

                        addToFavoriteResponse.observe(
                            view.findViewTreeLifecycleOwner()!!,
                            { addToFavorite ->
                                if (addToFavorite.success) {
                                    favoriteMovieItems.results?.add(position, movieToRemove)
                                    notifyItemInserted(position)
                                } else {
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Error while adding movie back",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    }
                    favoriteItemRemovedSnackbar.show()
                } else {
                    Toast.makeText(
                        holder.itemView.context,
                        "Error while removing from favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        cardView.setOnClickListener {
            val movieId = it.id.toLong()

            navController.navigate(
                FavoritesFragmentDirections.actionFragmentFavoritesToMovieInfoFragment(movieId)
            )
        }

        val listItem = favoriteMovieItems.results?.get(position)
        holder.bind(listItem!!)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}